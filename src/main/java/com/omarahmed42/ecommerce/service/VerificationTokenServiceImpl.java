package com.omarahmed42.ecommerce.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.joda.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omarahmed42.ecommerce.enums.TokenStatus;
import com.omarahmed42.ecommerce.event.OnRegistrationEvent;
import com.omarahmed42.ecommerce.exception.TokenAlreadyConsumedException;
import com.omarahmed42.ecommerce.exception.TokenExpiredException;
import com.omarahmed42.ecommerce.exception.TokenRevokedException;
import com.omarahmed42.ecommerce.exception.VerificationTokenNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.VerificationToken;
import com.omarahmed42.ecommerce.repository.UserRepository;
import com.omarahmed42.ecommerce.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public UUID addVerificationToken(User user) {
        return verificationTokenRepository.save(new VerificationToken(user)).getToken();
    }

    @Override
    @Transactional
    public void consumeVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findById(UUID.fromString(token))
                .orElseThrow(VerificationTokenNotFoundException::new);
        if (verificationToken.getStatus() == TokenStatus.EXPIRED) {
            throw new TokenExpiredException("Token has expired");
        } else if (verificationToken.getStatus() == TokenStatus.VALID && isExpired(verificationToken.getExpiryDate())) {
            expireToken(verificationToken);
            applicationEventPublisher.publishEvent(
                    new OnRegistrationEvent(userRepository.getReferenceById(verificationToken.getUser().getId())));
        } else if (verificationToken.getStatus() == TokenStatus.CONSUMED) {
            throw new TokenAlreadyConsumedException("This account has already been verified");
        } else if (verificationToken.getStatus() == TokenStatus.REVOKED) {
            throw new TokenRevokedException("This token has been revoked");
        } else {
            verifyAccount(verificationToken.getUser().getId());
        }
    }

    private boolean isExpired(Timestamp timestamp) {
        return timestamp.before(Instant.now().toDate());
    }

    private void expireToken(VerificationToken token) {
        token.setStatus(TokenStatus.EXPIRED);
        verificationTokenRepository.save(token);
    }

    private void verifyAccount(UUID userId) {
        User user = userRepository.getReferenceById(userId);
        user.setActive(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteVerificationToken(UUID token) {
        verificationTokenRepository.deleteById(token);
    }
}
