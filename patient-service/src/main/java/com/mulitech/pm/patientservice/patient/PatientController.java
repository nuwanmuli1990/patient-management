package com.mulitech.pm.patientservice.patient;

import com.mulitech.pm.patientservice.validators.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient API", description = "Endpoints for managing patients")
public class PatientController {

	private final PatientService patientService;

	@GetMapping
	@Operation(summary = "Create a new patient", description = "Adds a new patient to the system.")
	@ApiResponse(responseCode = "201", description = "Patient created successfully")
	@ApiResponse(responseCode = "400", description = "Invalid input")
	public ResponseEntity<List<PatientResponse>> findAllPatients() {
		return ResponseEntity.ok(patientService.findAllPatients());
	}

	@PostMapping
	public ResponseEntity<PatientResponse> createPatient(@RequestBody @Validated({Default.class,OnCreate.class}) PatientRequest patientRequest) {
		return ResponseEntity.ok(patientService.createPatient(patientRequest));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a patient", description = "Modifies an existing patient record.")
	@ApiResponse(responseCode = "200", description = "Patient updated successfully")
	@ApiResponse(responseCode = "404", description = "Patient not found")
	public ResponseEntity<PatientResponse> updatePatient(@PathVariable UUID id, @RequestBody @Validated({Default.class}) PatientRequest patientRequest) {
		return ResponseEntity.ok(patientService.updatePatient(id, patientRequest));
	}
}
