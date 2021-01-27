package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;

import java.util.Date;

public interface PasswordResetTokenService {
    Date expiryDate();

    boolean isExpired(PasswordResetToken passwordResetToken);

    PasswordResetToken createPasswordResetToken(User user);
}
