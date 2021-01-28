package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;

import java.util.Date;

public interface PasswordResetTokenService {
    boolean validatePasswordResetToken(String token);

    boolean isExpired(PasswordResetToken passwordResetToken);

    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken);
}
