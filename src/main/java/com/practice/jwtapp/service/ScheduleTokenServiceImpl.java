package com.practice.jwtapp.service;

import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class ScheduleTokenServiceImpl implements ScheduleTokenService {
    @Autowired
    private ConfirmAccountTokenRepository confirmAccountTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(cron = "${scheduled-token-delete.cron}")
    public void deleteExpiredTokens() {
        Date date = Date.from(Instant.now());
        confirmAccountTokenRepository.deleteAllExpiredConfirmAccountToken(date);
        passwordResetTokenRepository.deleteAllExpiredPasswordResetTokens(date);
    }
}
