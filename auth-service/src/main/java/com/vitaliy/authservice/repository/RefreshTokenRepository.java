package com.vitaliy.authservice.repository;

import com.vitaliy.authservice.domain.RefreshToken;
import com.vitaliy.authservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
