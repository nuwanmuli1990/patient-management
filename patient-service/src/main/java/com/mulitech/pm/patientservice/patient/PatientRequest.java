package com.mulitech.pm.patientservice.patient;

import com.mulitech.pm.patientservice.validators.OnCreate;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link Patient}
 */
@Builder
public record PatientRequest(
		@NotBlank(message = "First Name is required")
		@Size(message = "First Name cannot exceed 100 characters", min = 1, max = 30)
		String firstName,
		@NotBlank(message = "Last Name is required")
		@Size(message = "Last Name cannot exceed 100 characters", min = 1, max = 70)
		String lastName,
		@NotBlank(message = "Email is required")
		@Email(message = "Email is incorrect")
		@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
		String email,
		@NotBlank(message = "Date of birth is required")
		String dateOfBirth,
		@NotBlank(groups = OnCreate.class)
		String dateOfRegistration
) implements Serializable {
}