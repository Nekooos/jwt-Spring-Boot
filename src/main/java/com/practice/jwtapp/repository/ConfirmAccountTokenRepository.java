package com.practice.jwtapp.repository;

import com.practice.jwtapp.model.ConfirmAccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ConfirmAccountTokenRepository extends JpaRepository<ConfirmAccountToken, Long> {
    Optional<ConfirmAccountToken> findByToken(String token);

    @Modifying
    @Query("delete from ConfirmAccountToken c where c.expiryDate <= ?1")
    void deleteAllExpiredConfirmAccountToken(@Param("date")Date date);
}
