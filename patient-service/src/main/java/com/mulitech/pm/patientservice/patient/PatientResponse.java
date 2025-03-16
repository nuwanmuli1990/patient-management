package com.mulitech.pm.patientservice.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link Patient}
 */
@Builder
public record PatientResponse(UUID id, @NotNull String firstName, @NotNull String lastName,
                              @NotNull @Email String email, @NotNull LocalDateTime dateOfBirth,
                              @NotNull LocalDateTime dateOfRegistration) implements Serializable {
}