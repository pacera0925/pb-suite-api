package com.paulcera.pb_suite_api.security.model;

public class UserPrincipalMother {

    public static UserPrincipal admin() {
        return new UserPrincipal(WebUserMother.admin());
    }

}
