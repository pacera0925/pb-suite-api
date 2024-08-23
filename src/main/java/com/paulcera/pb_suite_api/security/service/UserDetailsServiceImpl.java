package com.paulcera.pb_suite_api.security.service;

import com.paulcera.pb_suite_api.security.model.UserPrincipal;
import com.paulcera.pb_suite_api.security.model.WebUser;
import com.paulcera.pb_suite_api.security.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WebUserRepository webUserRepository;

    @Autowired
    public UserDetailsServiceImpl(WebUserRepository webUserRepository) {
        this.webUserRepository = webUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebUser user = webUserRepository.findByUsername(username).orElseThrow();

        return new UserPrincipal(user);
    }
}
