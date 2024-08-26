package com.paulcera.pb_suite_api.security.controller;

import com.paulcera.pb_suite_api.security.dto.LoginRequest;
import com.paulcera.pb_suite_api.security.dto.ResponseMessage;
import com.paulcera.pb_suite_api.security.exception.AlreadyLoggedInException;
import com.paulcera.pb_suite_api.security.model.AuthenticationToken;
import com.paulcera.pb_suite_api.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            throw new AlreadyLoggedInException("Already logged in.");
        }

        AuthenticationToken token = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(new ResponseMessage("Successfully logged in.", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(HttpServletRequest request) {
        authenticationService.initiateLogout(request);

        return ResponseEntity.ok(new ResponseMessage("Successfully logged out."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseMessage> refresh(HttpServletRequest request) {
        AuthenticationToken token = authenticationService.issueNewToken(request);

        return ResponseEntity.ok(new ResponseMessage("New token issued.", token));
    }

}
