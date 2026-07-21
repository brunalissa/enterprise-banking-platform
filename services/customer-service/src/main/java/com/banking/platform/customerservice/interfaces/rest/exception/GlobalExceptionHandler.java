package com.banking.platform.customerservice.interfaces.rest.exception;

import com.banking.platform.customerservice.domain.exception.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(404, ex.getMessage()));
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(CustomerAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(409, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCustomerDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(InvalidCustomerDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(400, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ErrorResponse.of(400, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(500, "An unexpected error occurred"));
    }

    @Getter @Builder @AllArgsConstructor
    public static class ErrorResponse {
        private int status;
        private String message;
        private Map<String, String> details;
        private Instant timestamp;

        public static ErrorResponse of(int status, String message) {
            return ErrorResponse.builder().status(status).message(message).timestamp(Instant.now()).build();
        }

        public static ErrorResponse of(int status, String message, Map<String, String> details) {
            return ErrorResponse.builder().status(status).message(message).details(details).timestamp(Instant.now()).build();
        }
    }
}
