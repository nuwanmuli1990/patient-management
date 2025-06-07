package com.pm.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.Protocol;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.msk.CfnCluster;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocalStack extends Stack {

	private final Vpc vpc;
	private final Cluster ecsCluster;

	public LocalStack(final App scope, final String id, final StackProps props) {
		super(scope, id, props);
		this.vpc = createVpc();

		DatabaseInstance authServiceDb = createDatabaseInstance("authServiceDB", "auth-service-db");
		DatabaseInstance patientServiceDb = createDatabaseInstance("patientServiceDB", "patient-service-db");

		CfnHealthCheck authServiceHealthCheck = createHealthCheck(authServiceDb, "AuthServiceHealthCheck");
		CfnHealthCheck patientServiceHealthCheck = createHealthCheck(patientServiceDb, "PatientServiceHealthCheck");

		CfnCluster mskCluster = createMskCluster();

		this.ecsCluster = createEcsCluster();

		FargateService authService = createFargateService(
				"AuthService",
				"auth-service",
				List.of(4005),
				authServiceDb,
				Map.of("JWT_SECRET", "mWjHzKJ3pFlWk4OZ2N1DE4YzKMezTr1XSl1WaQlDNE8="));

		authService.getNode().addDependency(authServiceHealthCheck);
		authService.getNode().addDependency(authServiceDb);

		FargateService billingService = createFargateService("BillingService", "billing-service", List.of(4001, 9001), null, null);

		FargateService analyticService = createFargateService("AnalyticService", "analytic-service", List.of(4002), null, null);
		analyticService.getNode().addDependency(mskCluster);

		FargateService patientService = createFargateService("PatientService",
				"patient-service",
				List.of(4000),
				patientServiceDb,
				Map.of(
						"BILLING_SERVICE_ADDRESS", "host.docker.internal",
						"BILLING_SERVICE_GRPC_PORT", "9001"
				));
		patientService.getNode().addDependency(patientServiceDb);
		patientService.getNode().addDependency(patientServiceHealthCheck);
		patientService.getNode().addDependency(billingService);
		patientService.getNode().addDependency(mskCluster);

		createApiGatewayService();

	}

	private void createApiGatewayService(){
		FargateTaskDefinition taskDefinition = FargateTaskDefinition.Builder.create(this, "apiGateway")
				.cpu(256)
				.memoryLimitMiB(512)
				.build();

		ContainerDefinitionOptions  containerDefinitionOptions = ContainerDefinitionOptions.builder()
				.image(ContainerImage.fromRegistry("api-gateway"))
				.environment(Map.of(
						"SPRING_PROFILES_ACTIVE","prod",
						"AUTH_SERVICE_URL", "http://host.docker.internal:4005"
				))
				.portMappings(List.of(4004).stream().map(port -> PortMapping.builder()
						.containerPort(port)
						.hostPort(port)
						.protocol(Protocol.TCP)
						.build()).toList())
				.logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
						.logGroup(LogGroup.Builder.create(this, "ApiGatewayLogGroup")
								.logGroupName("/ecs/api-gateway")
								.removalPolicy(RemovalPolicy.DESTROY)
								.retention(RetentionDays.ONE_DAY)
								.build())
						.streamPrefix("api-gateway")
						.build()))
				.build();

		taskDefinition.addContainer("ApiGatewayContainer", containerDefinitionOptions);

		ApplicationLoadBalancedFargateService apiGateway = ApplicationLoadBalancedFargateService.Builder
				.create(this, "ApiGateWayService")
				.cluster(ecsCluster)
				.serviceName("api-gateway")
				.taskDefinition(taskDefinition)
				.desiredCount(1)
				.healthCheckGracePeriod(Duration.seconds(60))
				.build();
	}

	public static void main(final String[] args) {
		App app = new App(AppProps.builder().outdir("./cdk.out").build());
		StackProps props = StackProps.builder()
				.synthesizer(new BootstraplessSynthesizer())
				.build();

		new LocalStack(app, "localstack", props);
		app.synth();
		System.out.println("App synthesizing in progress :");
	}

	private Cluster createEcsCluster() {
		return Cluster.Builder.create(this, "PatientManagementEcsCluster")
				.vpc(this.vpc)
				.defaultCloudMapNamespace(CloudMapNamespaceOptions.builder()
						.name("patient-management.local")
						.build())
				.build();
	}

	private Vpc createVpc() {
		return Vpc.Builder
				.create(this, "PatientManagementVpc")
				.vpcName("PatientManagementVpc")
				.maxAzs(2)
				.build();
	}

	private DatabaseInstance createDatabaseInstance(String id, String dbName) {
		return DatabaseInstance.Builder
				.create(this, id)
				.engine((DatabaseInstanceEngine.postgres(
						PostgresInstanceEngineProps.builder()
								.version(PostgresEngineVersion.VER_17_2)
								.build())))
				.vpc(this.vpc)
				.instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
				.allocatedStorage(20)
				.databaseName(dbName)
				.removalPolicy(RemovalPolicy.DESTROY)
				.credentials(Credentials.fromGeneratedSecret("admin_user"))
				.build();
	}

	private CfnHealthCheck createHealthCheck(DatabaseInstance db, String id) {
		return CfnHealthCheck.Builder.create(this, id)
				.healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
						.type("TCP")
						.port(Token.asNumber(db.getDbInstanceEndpointPort()))
						.ipAddress(db.getDbInstanceEndpointAddress())
						.failureThreshold(3)
						.requestInterval(30)
						.build())
				.build();
	}

	private CfnCluster createMskCluster() {
		return CfnCluster.Builder.create(this, "MskCluster")
				.clusterName("kafka-cluster")
				.kafkaVersion("2.8.0")
				.numberOfBrokerNodes(4)
				.brokerNodeGroupInfo(CfnCluster.BrokerNodeGroupInfoProperty.builder()
						.instanceType("kafka.m5.xlarge")
						.clientSubnets(this.vpc.getPrivateSubnets().stream().map(ISubnet::getSubnetId).toList())
						.brokerAzDistribution("DEFAULT")
						.build())
				.build();
	}

	private FargateService createFargateService(String id, String imageName, List<Integer> ports, DatabaseInstance db, Map<String, String> additionalEnvVars) {
		FargateTaskDefinition taskDefinition = FargateTaskDefinition.Builder.create(this, id+"Task")
				.cpu(256)
				.memoryLimitMiB(512)
				.build();

		ContainerDefinitionOptions.Builder  containerDefinitionOptionsBuilder = ContainerDefinitionOptions.builder()
				.image(ContainerImage.fromRegistry(imageName))
				.portMappings(ports.stream().map(port -> PortMapping.builder()
						.containerPort(port)
						.hostPort(port)
						.protocol(Protocol.TCP)
						.build()).toList())
				.logging(LogDriver.awsLogs(AwsLogDriverProps.builder()
						.logGroup(LogGroup.Builder.create(this, id + "LogGroup")
								.logGroupName("/ecs/" + id + imageName)
								.removalPolicy(RemovalPolicy.DESTROY)
								.retention(RetentionDays.ONE_DAY)
								.build())
								.streamPrefix(imageName)
						.build()));

		Map<String,String> enVars = new HashMap<>();
		enVars.put("SPRING_KAFKA_BOOTSTRAP_SERVERS", "localhost.localstack.cloud:4510, localhost.localstack.cloud:4511, localhost.localstack.cloud:4512");

		if(additionalEnvVars != null){
			enVars.putAll(additionalEnvVars);
		}

		if(db != null){
			enVars.put("SPRING_DATASOURCE_URL","jdbc:postgresql://%s:%s/%s-db".formatted(
					db.getDbInstanceEndpointAddress(),
					db.getDbInstanceEndpointPort(),
					imageName));

			enVars.put("SPRING_DATASOURCE_USERNAME","admin_user");
			enVars.put("SPRING_DATASOURCE_PASSWORD", Objects.requireNonNull(db.getSecret()).secretValueFromJson("password").toString());
			enVars.put("SPRING_JPA_HIBERNATE_DDL_AUTO","update");
			enVars.put("SPRING_JPA_HIBERNATE_SHOW_SQL","true");
			enVars.put("SPRING_SQL_INIT_MODE","always");
			enVars.put("SPRING_DATASOURCE_HIKARI_INITIALIZATION_FAIL_TIMEOUT","600000");
		}

		containerDefinitionOptionsBuilder.environment(enVars);
		taskDefinition.addContainer(imageName+"Container", containerDefinitionOptionsBuilder.build());

		return FargateService.Builder.create(this, id)
				.cluster(ecsCluster)
				.taskDefinition(taskDefinition)
				.assignPublicIp(false)
				.serviceName(imageName)
				.build();
	}
}
