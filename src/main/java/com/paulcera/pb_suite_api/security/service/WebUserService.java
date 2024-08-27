package com.paulcera.pb_suite_api.security.service;

import com.paulcera.pb_suite_api.security.dto.WebUserForm;
import com.paulcera.pb_suite_api.security.dto.WebUserView;
import com.paulcera.pb_suite_api.security.model.WebUser;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WebUserService {

    private final WebUserRepository webUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebUserService(WebUserRepository webUserRepository, PasswordEncoder passwordEncoder) {
        this.webUserRepository = webUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public WebUserView create(WebUserForm form) {
        WebUser newWebUser = webUserRepository.save(WebUser.createFromForm(form, passwordEncoder));
        return new WebUserView(newWebUser);
    }

}
