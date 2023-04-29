package com.omarahmed42.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class RegistrationController {
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmRegistration(@RequestParam(value = "token") String token) {
        verificationTokenService.consumeVerificationToken(token);
        return ResponseEntity.ok().build();
    }
}
