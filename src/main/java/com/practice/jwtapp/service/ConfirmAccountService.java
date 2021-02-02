package com.practice.jwtapp.service;

import com.practice.jwtapp.model.ConfirmAccountToken;
import com.practice.jwtapp.model.User;

public interface ConfirmAccountService {
    ConfirmAccountToken findConfirmAccountTokenByToken(String token);

    void validateConfirmAccountToken(String token);

    ConfirmAccountToken createConfirmAccountToken(User user);

    ConfirmAccountToken saveConfirmAccountToken(ConfirmAccountToken confirmAccountToken);
}
