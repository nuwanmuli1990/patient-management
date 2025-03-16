package com.mulitech.pm.patientservice.patient;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PatientMapper {

	public List<PatientResponse> toPatientResponceList(List<Patient> patients) {
		return patients.stream().map(this::toPatientResponse).toList();
	}

	public Patient toPatient(PatientRequest patientRequest) {
		Patient patient = new Patient();
		patient.setFirstName(patientRequest.firstName());
		patient.setLastName(patientRequest.lastName());
		patient.setEmail(patientRequest.email());
		patient.setDateOfBirth(LocalDateTime.parse(patientRequest.dateOfBirth()));
		patient.setDateOfRegistration(LocalDateTime.parse(patientRequest.dateOfRegistration()));
		return patient;
	}

	public PatientResponse toPatientResponse(Patient patient) {
		return PatientResponse.builder()
				.id(patient.getId())
				.firstName(patient.getFirstName())
				.lastName(patient.getLastName())
				.email(patient.getEmail())
				.dateOfBirth(patient.getDateOfBirth())
				.dateOfRegistration(patient.getDateOfRegistration())
				.build();
	}
}
