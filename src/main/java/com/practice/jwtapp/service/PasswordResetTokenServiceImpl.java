package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Value("${jwt.password-expiration}")
    private int passwordResetTokenExpiration;

    public Date expiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, passwordResetTokenExpiration);
        return calendar.getTime();
    }

    public boolean isExpired(PasswordResetToken passwordResetToken) {
        return new Date().after(passwordResetToken.getExpiryDate());
    }

    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(expiryDate());
        return passwordResetToken;
    }
}
