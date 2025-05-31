package com.mulitech.pm.authservice.user.auth;

import com.mulitech.pm.authservice.user.LoginRequest;
import com.mulitech.pm.authservice.user.UserService;
import com.mulitech.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

	private final UserService userService;
	private final PasswordEncoder  passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public Optional<String> authenticate(LoginRequest loginRequest) {

		return userService.findByEmail(loginRequest.email())
				.filter(user -> passwordEncoder.matches(loginRequest.password(),user.getPassword()))
				.map(user -> jwtUtil.generateToken(user.getEmail(), user.getRole()));
	}

	public boolean validateToken(String token) {
		try {
			jwtUtil.validateToken(token);
			return true;
		} catch (JwtException ex){
			return false;
		}
	}
}
