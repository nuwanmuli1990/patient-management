package com.mulitech.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {

	private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

	public BillingServiceGrpcClient(
			@Value("${billing.service.address:localhost}") String address,
			@Value("${billing.service.grpc.port:9001}") int grpcServerPort
	) {
		ManagedChannel chanel = ManagedChannelBuilder.forAddress(address, grpcServerPort).usePlaintext().build();
		billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(chanel);
	}

	public BillingResponse createBillingAccount(String patientId, String name, String email) {
		BillingRequest billingRequest = BillingRequest.newBuilder()
				.setPatientId(patientId).setName(name).setEmail(email).build();

		BillingResponse billingResponse = billingServiceBlockingStub.createBillingAccount(billingRequest);
		log.info("Received response from Billing Service via GRPC: {} ", billingResponse);
		return billingResponse;
	}
}
