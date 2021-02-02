package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.AccountTokenNotFoundException;
import com.practice.jwtapp.exception.AccountTokenNotValidException;
import com.practice.jwtapp.model.ConfirmAccountToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class ConfirmAccountServiceImpl implements ConfirmAccountService {
    @Value("${jwt.confirm-account-expiration}")
    private int confirmAccountExpiration;

    @Autowired
    private ConfirmAccountTokenRepository confirmAccountTokenRepository;

    public ConfirmAccountToken findConfirmAccountTokenByToken(String token) {
        return confirmAccountTokenRepository.findByToken(token)
                .orElseThrow(() -> new AccountTokenNotFoundException("Url is not valid or expired"));
    }

    public void validateConfirmAccountToken(String token) {
        final ConfirmAccountToken confirmAccountToken = confirmAccountTokenRepository.findByToken(token)
                .orElseThrow(() -> new AccountTokenNotFoundException("Url is not valid or expired"));

        if(isExpired(confirmAccountToken.getExpiryDate())) {
            throw new AccountTokenNotValidException("Url is not valid or expired");
        }
    }

    public ConfirmAccountToken saveConfirmAccountToken(ConfirmAccountToken confirmAccountToken) {
        return confirmAccountTokenRepository.save(confirmAccountToken);
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
        confirmAccountToken.setExpiryDate(expiryDate());
        confirmAccountToken.setToken(UUID.randomUUID().toString());
        confirmAccountToken.setUser(user);
        return confirmAccountToken;
    }
}
