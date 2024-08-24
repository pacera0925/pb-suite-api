package com.paulcera.pb_suite_api.security.service;

import com.paulcera.pb_suite_api.security.dto.WebUserForm;
import com.paulcera.pb_suite_api.security.dto.WebUserView;
import com.paulcera.pb_suite_api.security.model.WebUser;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WebUserService {

    private final WebUserRepository webUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public WebUserService(WebUserRepository webUserRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager) {
        this.webUserRepository = webUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public WebUserView create(WebUserForm form) {
        WebUser newWebUser = webUserRepository.save(WebUser.createFromForm(form, passwordEncoder));
        return new WebUserView(newWebUser);
    }

    public boolean isUserCredentialsValid(WebUser webUser) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(webUser.getUsername(), webUser.getPassword()));

        return authentication.isAuthenticated();
    }
}
