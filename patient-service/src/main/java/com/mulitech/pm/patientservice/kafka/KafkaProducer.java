package com.mulitech.pm.patientservice.kafka;

import com.mulitech.pm.patientservice.patient.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;

@Slf4j
@Component
public class KafkaProducer {

	private final KafkaTemplate<String, byte[]> kafkaTemplate;
	public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendEvent(Patient patient) {
		PatientEvent patientEvent =  PatientEvent.newBuilder()
				.setPatientId(patient.getId().toString())
				.setName(patient.getFirstName() + " "+ patient.getLastName())
				.setEmail(patient.getEmail())
				.setEventType("PATIENT_CREATED")
				.build();

		try {
			kafkaTemplate.send("patient", patientEvent.toByteArray());
		}catch (Exception e) {
			log.error("Error in sending event {}", patientEvent);
		}

	}
}
