package com.omarahmed42.ecommerce.service;

import java.util.UUID;

import com.omarahmed42.ecommerce.model.User;

public interface VerificationTokenService {
    UUID addVerificationToken(User user);

    void deleteVerificationToken(UUID token);

    void consumeVerificationToken(String token);
}
