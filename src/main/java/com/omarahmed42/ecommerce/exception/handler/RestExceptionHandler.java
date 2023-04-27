package com.omarahmed42.ecommerce.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.omarahmed42.ecommerce.exception.AlreadyExistsException;
import com.omarahmed42.ecommerce.exception.NotFoundException;
import com.omarahmed42.ecommerce.exception.UnauthorizedAccessException;
import com.omarahmed42.ecommerce.exception.ValidationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsException.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessExpcetion(
            UnauthorizedAccessException unauthorizedAccessException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(unauthorizedAccessException.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        return ResponseEntity.status(ValidationException.STATUS_CODE).body(validationException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}
