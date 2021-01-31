package com.practice.jwtapp.service;

import com.practice.jwtapp.model.ConfirmAccountToken;
import com.practice.jwtapp.model.User;

public interface ConfirmAccountService {
    ConfirmAccountToken createConfirmAccountToken(User user);

    void saveConfirmAccountToken(ConfirmAccountToken confirmAccountToken);
}
