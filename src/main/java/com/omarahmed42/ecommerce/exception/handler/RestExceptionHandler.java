package com.omarahmed42.ecommerce.exception.handler;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omarahmed42.ecommerce.exception.AlreadyExistsException;
import com.omarahmed42.ecommerce.exception.BadUsernameException;
import com.omarahmed42.ecommerce.exception.NotFoundException;
import com.omarahmed42.ecommerce.exception.TokenAlreadyConsumedException;
import com.omarahmed42.ecommerce.exception.TokenExpiredException;
import com.omarahmed42.ecommerce.exception.TokenRevokedException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.exception.ValidationException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Data
    private static class ErrorMessage implements Serializable {
        private String message;
        private boolean success = false;

        public ErrorMessage(String message) {
            this.message = message;
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException) {
        logError(notFoundException);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(notFoundException.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        logError(alreadyExistsException);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(alreadyExistsException.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessExpcetion(
            UnauthorizedAccessException unauthorizedAccessException) {
        logError(unauthorizedAccessException);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage(unauthorizedAccessException.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        logError(validationException);
        return ResponseEntity.status(ValidationException.STATUS_CODE)
                .body(new ErrorMessage(validationException.getMessage()));
    }

    @ExceptionHandler(TokenAlreadyConsumedException.class)
    public ResponseEntity<Object> handleTokenAlreadyConsumedException(
            TokenAlreadyConsumedException tokenAlreadyConsumedException) {
        logError(tokenAlreadyConsumedException);
        return ResponseEntity.status(TokenAlreadyConsumedException.STATUS_CODE)
                .body(new ErrorMessage(tokenAlreadyConsumedException.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException tokenExpiredException) {
        logError(tokenExpiredException);
        return ResponseEntity.status(TokenExpiredException.STATUS_CODE)
                .body(new ErrorMessage(tokenExpiredException.getMessage()));
    }

    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<Object> handleTokenRevokedException(TokenRevokedException tokenRevokedException) {
        logError(tokenRevokedException);
        return ResponseEntity.badRequest().body(new ErrorMessage(tokenRevokedException.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException badCredentialsException) {
        logError(badCredentialsException);
        return ResponseEntity.status(401).body(new ErrorMessage("Incorrect email or password"));
    }

    @ExceptionHandler(BadUsernameException.class)
    public ResponseEntity<ErrorMessage> handleBadUsernameException(BadUsernameException badUsernameException) {
        logError(badUsernameException);
        return ResponseEntity.status(401).body(new ErrorMessage("Incorrect email or password"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        logError(accessDeniedException);
        return ResponseEntity.status(403).body(new ErrorMessage("Access Denied"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logError(e);
        return ResponseEntity.internalServerError().build();
    }

    private void logError(Exception e) {
        log.error(e.getMessage(), e);
    }
}
