package com.springboot.example.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.example.restful.dto.v1.AccountCredentialsDTO;
import com.springboot.example.restful.services.AuthServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication EndPoints", description = "Operations pertaining to authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @Operation(summary = "Authenticate user", description = "Authenticate user with username and password and return a token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials) {
        if (isNotNull(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Username and password are required");
        }
        var token = authServices.signin(credentials);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/password supplied");
        }
        return token;
    }

	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity<?> refreshToken(@PathVariable("username") String username,
			@RequestHeader("Authorization") String refreshToken) {
		if (isNotNull(username, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		var token = authServices.refresh(username, refreshToken);
		if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		return token;
	}

    private boolean isNotNull(AccountCredentialsDTO credentials) {
        return credentials == null || credentials.getUsername() == null || credentials.getPassword() == null || credentials.getUsername().isEmpty()
                || credentials.getPassword().isEmpty() || credentials.getUsername().isBlank() || credentials.getPassword().isBlank();
    }

	private boolean isNotNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() ||
				username == null || username.isBlank();
	}

}
