package com.mulitech.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

@Component
public class KafkaConsumer {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(topics = "patient", groupId = "analytics-service")
	public void receivePatientEvents(byte[] events) {
		try {
			PatientEvent patientEvent = PatientEvent.parseFrom(events);
			log.info("Received patient event : [PatientId={}, PatientName={}]", patientEvent.getPatientId(), patientEvent.getName());
		} catch (InvalidProtocolBufferException e) {
			log.error("Error deserializing event {}", events);
		}
	}
}
