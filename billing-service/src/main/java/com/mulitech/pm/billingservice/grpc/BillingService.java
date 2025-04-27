package com.mulitech.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class BillingService extends BillingServiceImplBase {

	@Override
	public void createBillingAccount(billing.BillingRequest billingRequest,
	                                 StreamObserver<BillingResponse> responseObserver) {
		log.info("Billing request received: {}", billingRequest.toString());

		BillingResponse billingResponse = BillingResponse.newBuilder()
				.setAccountId("12345")
				.setStatus("ACTIVE")
				.build();

		responseObserver.onNext(billingResponse);
		responseObserver.onCompleted();
	}
}
