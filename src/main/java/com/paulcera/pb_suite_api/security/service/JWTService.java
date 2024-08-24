package com.paulcera.pb_suite_api.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

}
