package com.practice.jwtapp.repository;

import com.practice.jwtapp.model.ConfirmAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmAccountTokenRepository extends JpaRepository<ConfirmAccountToken, Long> {

}
