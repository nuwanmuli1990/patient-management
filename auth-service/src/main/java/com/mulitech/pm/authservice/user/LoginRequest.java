package com.mulitech.pm.authservice.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		@NotBlank(message = "Email is required")
		@Email(message = "Email should be a valid email address")
		String email,

		@NotBlank(message = "Password is required")
		@Size(min = 8, message = "Password should be at least 8 characters long")
		String password
) {
}
