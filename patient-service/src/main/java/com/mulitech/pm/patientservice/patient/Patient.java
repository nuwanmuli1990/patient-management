package com.mulitech.pm.patientservice.patient;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	@Email
	@Column(nullable = false, unique = true)
	private String email;

	@NotNull
	private LocalDateTime dateOfBirth;

	@NotNull
	private LocalDateTime dateOfRegistration;
}
