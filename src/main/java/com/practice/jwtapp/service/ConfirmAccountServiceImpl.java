package com.practice.jwtapp.service;

import com.practice.jwtapp.model.ConfirmAccountToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ConfirmAccountServiceImpl implements ConfirmAccountService {
    @Value("${jwt.confirm-account-expiration}")
    private int confirmAccountExpiration;

    @Autowired
    private ConfirmAccountTokenRepository confirmAccountTokenRepository;

    public void saveConfirmAccountToken(ConfirmAccountToken confirmAccountToken) {
        confirmAccountTokenRepository.save(confirmAccountToken);
    }

    private Date expiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, confirmAccountExpiration);
        return calendar.getTime();
    }

    private boolean isExpired(Date date) {
        return new Date().after(date);
    }

    public ConfirmAccountToken createConfirmAccountToken(User user) {
        ConfirmAccountToken confirmAccountToken = new ConfirmAccountToken();
        confirmAccountToken.setCreatedDate(expiryDate());
        confirmAccountToken.setToken(UUID.randomUUID().toString());
        confirmAccountToken.setUser(user);
        return confirmAccountToken;
    }
}
