package com.banking.platform.frauddetectionservice.interfaces.rest.exception;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.error("Error: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(404, ex.getMessage()));
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
        private Instant timestamp;
        public static ErrorResponse of(int status, String message) {
            return ErrorResponse.builder().status(status).message(message).timestamp(Instant.now()).build();
        }
    }
}
