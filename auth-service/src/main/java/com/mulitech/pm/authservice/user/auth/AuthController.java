package com.mulitech.pm.authservice.user.auth;

import com.mulitech.pm.authservice.user.LoginRequest;
import com.mulitech.pm.authservice.user.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "Generate token on user login")
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		Optional<String> tokenOpt = authService.authenticate(loginRequest);
		if (tokenOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		String token = tokenOpt.get();
		return ResponseEntity.ok(new LoginResponse(token));
	}

	@Operation(summary = "Validate Token")
	@GetMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
		// Authorization: Bearer <Token>
		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return authService.validateToken(authHeader.substring(7))
				? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
