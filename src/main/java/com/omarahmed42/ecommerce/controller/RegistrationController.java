package com.omarahmed42.ecommerce.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.exception.VerificationTokenNotFoundException;
import com.omarahmed42.ecommerce.model.User;
import com.omarahmed42.ecommerce.model.VerificationToken;
import com.omarahmed42.ecommerce.service.UserService;
import com.omarahmed42.ecommerce.service.VerificationTokenService;

@RestController
@RequestMapping("/v1")
public class RegistrationController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    public RegistrationController(UserService userService, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam(value = "token") String token) {
        try {
            VerificationToken verificationToken = verificationTokenService.getVerificationTokenByToken(token);
            User user = verificationToken.getUser();

            if (Objects.isNull(user)) {
                return ResponseEntity.status(404).build();
            }

            if (isExpired(verificationToken.getExpiryDate())) {
                return ResponseEntity.status(410).build();
            }

            user.setEnabled(true);
            user.setActive(true);
            userService.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (VerificationTokenNotFoundException verificationTokenNotFound) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isExpired(Timestamp expiryDate) {
        return expiryDate.getTime() - Calendar.getInstance().getTime().getTime() <= 0;
    }
}
