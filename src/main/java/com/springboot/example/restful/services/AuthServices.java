package com.springboot.example.restful.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.example.restful.dto.v1.AccountCredentialsDTO;
import com.springboot.example.restful.dto.v1.TokenDTO;
import com.springboot.example.restful.repositories.UserRepository;
import com.springboot.example.restful.securityJwt.JwtTokenProvider;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> signin(AccountCredentialsDTO credentials) {
        
        try {
            var userName = credentials.getUsername();
            var password = credentials.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            var user = userRepository.findByUsername(userName);
            var tokenResponse = new TokenDTO();
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
            } else {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username/password supplied");
        }
    }

    public ResponseEntity<?> refresh(String username, String refreshToken) {
        var user = userRepository.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(tokenProvider.refreshToken(refreshToken));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}
