package com.paulcera.pb_suite_api.security.controller;

import com.paulcera.pb_suite_api.security.dto.LoginRequest;
import com.paulcera.pb_suite_api.security.model.AuthenticationToken;
import com.paulcera.pb_suite_api.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationToken> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationToken token = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationToken> refresh() {
        return ResponseEntity.ok(null);
    }

}
