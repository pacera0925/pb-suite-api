package com.paulcera.pb_suite_api.security.repository;

import com.paulcera.pb_suite_api.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

}
