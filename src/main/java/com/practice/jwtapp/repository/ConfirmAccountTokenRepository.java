package com.practice.jwtapp.repository;

import com.practice.jwtapp.model.ConfirmAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmAccountTokenRepository extends JpaRepository<ConfirmAccountToken, Long> {
    Optional<ConfirmAccountToken> findByToken(String token);
}
