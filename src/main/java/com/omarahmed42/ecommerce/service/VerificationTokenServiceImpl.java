package com.omarahmed42.ecommerce.service;

import org.springframework.stereotype.Service;

import com.omarahmed42.ecommerce.exception.VerificationTokenNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.VerificationToken;
import com.omarahmed42.ecommerce.repository.VerificationTokenRepository;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }


    @Override
    public void addVerificationToken(String token, User user) {
        verificationTokenRepository.save(new VerificationToken(token, user));
    }

    @Override
    public void addVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void deleteVerificationToken(Long id) {
        verificationTokenRepository.deleteById(id);
    }

    @Override
    public void updateVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository
                .findById(verificationToken.getId())
                .ifPresent(presentToken -> verificationTokenRepository.save(verificationToken));
    }

    @Override
    public VerificationToken getVerificationTokenByToken(String token) {
        return verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> {
                    throw new VerificationTokenNotFoundException("Verification token not found");
                });
    }
}
