package br.com.challenge.adapter.in.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.challenge.adapter.in.rest.response.ErrorResponse;
import br.com.challenge.domain.exception.InvalidCardException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidCardException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCardException(
        InvalidCardException exception
    ) {
        ErrorResponse errorResponse =
            new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exception.getErrors(),
                LocalDateTime.now()
            );
        
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception exception
    ) {
        ErrorResponse errorResponse =
            new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of("An unexpected error occurred"),
                LocalDateTime.now()
            );
        
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }
}
