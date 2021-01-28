package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.PasswordResetTokenNotFoundException;
import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Value("${jwt.password-expiration}")
    private int passwordResetTokenExpiration;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public boolean validatePasswordResetToken(String token) {
        final PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new PasswordResetTokenNotFoundException("Url is not valid or expired"));

        return isExpired(passwordResetToken);
    }

    private Date expiryDate() {
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

    public PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }
}
