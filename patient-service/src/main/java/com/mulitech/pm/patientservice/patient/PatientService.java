package com.mulitech.pm.patientservice.patient;

import com.mulitech.pm.patientservice.exception.PatientExistException;
import com.mulitech.pm.patientservice.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepository patientRepository;
	private final PatientMapper patientMapper;

	public List<PatientResponse> findAllPatients() {
		return patientMapper.toPatientResponceList(patientRepository.findAll());
	}

	public PatientResponse createPatient(PatientRequest patientRequest) {
		boolean emailExist = patientRepository.existsByEmail(patientRequest.email());
		if (emailExist) {
			throw new PatientExistException("Email already exist :"+patientRequest.email());
		}
		Patient patient = patientRepository.save(patientMapper.toPatient(patientRequest));
		return patientMapper.toPatientResponse(patient);
	}

	public PatientResponse updatePatient(UUID id, PatientRequest patientRequest) {

		Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: "+ id));
		boolean emailExist = patientRepository.existsByEmail(patientRequest.email());
		if (emailExist && !patient.getEmail().equals(patientRequest.email())) {
			throw new PatientExistException("Email not exist :"+patientRequest.email());
		}

		patient.setFirstName(patientRequest.firstName());
		patient.setLastName(patientRequest.lastName());
		patient.setDateOfBirth(LocalDateTime.parse(patientRequest.dateOfBirth()));
		patient.setEmail(patientRequest.email());

		Patient updatedPatient = patientRepository.save(patient);
		return patientMapper.toPatientResponse(updatedPatient);
	}
}
