package com.paulcera.pb_suite_api.security.service;

import com.paulcera.pb_suite_api.security.dto.LoginRequest;
import com.paulcera.pb_suite_api.security.exception.InvalidCredentialsException;
import com.paulcera.pb_suite_api.security.model.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationToken authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateAccessToken(loginRequest.getUsername());
        String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());

        return new AuthenticationToken(accessToken, refreshToken);
    }

}
