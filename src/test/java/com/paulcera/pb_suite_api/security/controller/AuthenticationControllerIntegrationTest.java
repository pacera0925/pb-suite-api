package com.paulcera.pb_suite_api.security.controller;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulcera.pb_suite_api.security.dto.LoginRequest;
import com.paulcera.pb_suite_api.security.dto.LoginRequestMother;
import com.paulcera.pb_suite_api.security.filter.JWTFilter;
import com.paulcera.pb_suite_api.security.model.RefreshToken;
import com.paulcera.pb_suite_api.security.model.WebUser;
import com.paulcera.pb_suite_api.security.model.WebUserMother;
import com.paulcera.pb_suite_api.security.repository.RefreshTokenRepository;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import com.paulcera.pb_suite_api.security.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/authentication-controller-dataset.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilter(new JWTFilter(jwtService, userDetailsService))
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();

        refreshTokenRepository.deleteAll();
    }

    @Test
    void login_validCredentials_successMessage() throws Exception {
        LoginRequest loginRequest = LoginRequestMother.admin();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully logged in."))
            .andExpect(jsonPath("$.content.access_token").isNotEmpty())
            .andExpect(jsonPath("$.content.refresh_token").isNotEmpty());
    }

    @Test
    void login_invalidCredentials_badCredentialsError() throws Exception {
        LoginRequest loginRequest = LoginRequestMother.adminIncorrect();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Bad credentials"))
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void login_hasValidAuthBearerToken_alreadyLoggedInError() throws Exception {
        LoginRequest loginRequest = LoginRequestMother.admin();
        String token = "Bearer " + jwtService.generateAccessToken(loginRequest.getUsername());

        mockMvc.perform(post("/api/auth/login")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Already logged in."))
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void logout_notExistingRefreshToken_tokenNotFoundError() throws Exception {
        String token = "Bearer " + jwtService.generateAccessToken(WebUserMother.admin().getUsername());

        mockMvc.perform(post("/api/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", startsWith("No RefreshToken found with value: ")))
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void logout_validRefreshToken_success() throws Exception {
        WebUser webUser = webUserRepository.findById(1)
            .orElseThrow(() -> new IllegalStateException("Expected dataset should contain this web_user"));
        String refreshToken = jwtService.generateRefreshToken(webUser);
        String bearerToken = "Bearer " + refreshToken;

        mockMvc.perform(post("/api/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Successfully logged out."))
            .andExpect(jsonPath("$.content").isEmpty());

        RefreshToken updatedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new IllegalStateException("Expected dataset should contain this refresh_token"));

        assertTrue(updatedRefreshToken.isRevoked());
    }

    @Test
    void refresh_invalidRefreshToken_invalidRefreshTokenError() throws Exception {
        String bearerToken = "Bearer " + jwtService.generateAccessToken(WebUserMother.admin().getUsername());

        mockMvc.perform(post("/api/auth/refresh")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("RefreshToken is not valid."))
            .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void refresh_validRefreshToken_success() throws Exception {
        WebUser webUser = webUserRepository.findById(1)
            .orElseThrow(() -> new IllegalStateException("Expected dataset should contain this web_user"));
        String refreshToken = jwtService.generateRefreshToken(webUser);
        String bearerToken = "Bearer " + refreshToken;

        mockMvc.perform(post("/api/auth/refresh")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("New token issued."))
            .andExpect(jsonPath("$.content.access_token").isNotEmpty())
            .andExpect(jsonPath("$.content.refresh_token").isNotEmpty());
    }

}