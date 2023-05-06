package com.omarahmed42.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omarahmed42.ecommerce.service.VerificationTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1", consumes = "application/json", produces = "application/json")
@Tags(@Tag(name = "Registration"))
@RequiredArgsConstructor
public class RegistrationController {
        private final VerificationTokenService verificationTokenService;

        @GetMapping(value = "/confirm", consumes = "*/*")
        @Operation(summary = "Confirms registration by consuming a verification token")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "404", description = "Verification token not found"),
                        @ApiResponse(responseCode = "400", description = "Token has expired"),
                        @ApiResponse(responseCode = "400", description = "This account has already been verified"),
                        @ApiResponse(responseCode = "400", description = "This token has been revoked")

        })
        public ResponseEntity<Void> confirmRegistration(
                        @Parameter(name = "token", description = "verification token") @RequestParam(value = "token") String token) {
                verificationTokenService.consumeVerificationToken(token);
                return ResponseEntity.ok().build();
        }
}
