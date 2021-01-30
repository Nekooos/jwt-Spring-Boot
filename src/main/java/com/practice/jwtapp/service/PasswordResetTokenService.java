package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;

public interface PasswordResetTokenService {
    void validatePasswordResetToken(String token);

    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken);
}
