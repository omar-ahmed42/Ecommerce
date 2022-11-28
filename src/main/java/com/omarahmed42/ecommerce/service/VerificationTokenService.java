package com.omarahmed42.ecommerce.service;

import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.VerificationToken;

public interface VerificationTokenService {
    void addVerificationToken(String token, User user);
    void addVerificationToken(VerificationToken verificationToken);
    void deleteVerificationToken(Long id);
    void updateVerificationToken(VerificationToken verificationToken);
    VerificationToken getVerificationTokenByToken(String token);
}
