package com.paulcera.pb_suite_api.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationToken(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken
) {}
