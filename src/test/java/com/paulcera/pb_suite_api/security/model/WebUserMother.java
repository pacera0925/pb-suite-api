package com.paulcera.pb_suite_api.security.model;

public class WebUserMother {

    public static WebUser admin() {
        WebUser webUser = new WebUser();
        webUser.setUsername("admin");
        webUser.setPassword("admin");
        FullName fullName = new FullName();
        fullName.setFirstName("PB");
        fullName.setLastName("Admin");
        webUser.setFullName(fullName);
        return webUser;
    }
}
