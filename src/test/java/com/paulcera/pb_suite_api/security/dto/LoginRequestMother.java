package com.paulcera.pb_suite_api.security.dto;

public class LoginRequestMother {

    public static LoginRequest admin() {
        return new LoginRequest("admin", "admin");
    }

    public static LoginRequest adminIncorrect() {
        return new LoginRequest("admin", "adminx");
    }

}
