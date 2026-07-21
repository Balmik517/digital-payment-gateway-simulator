package com.balmik.dpgs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(
            EmailAlreadyExistsException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage()
                ));
    }


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFound(OrderNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("success", false,
                       "message", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<?> handlePaymentNotFound(PaymentNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("success", false,
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<?> handlePaymentAlreadyExists(PaymentAlreadyExistsException ex){

        return ResponseEntity.badRequest().body(
                Map.of("success", false,
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(PaymentAlreadyProcessedException.class)
    public ResponseEntity<?> handlePaymentAlreadyProcessed(PaymentAlreadyProcessedException ex){

        return ResponseEntity.badRequest().body(
                Map.of("success", false,
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(ResourceAccessDeniedException ex){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("success", false,
                        "message", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("success", false,
                        "message", ex.getMessage()));
    }
}
