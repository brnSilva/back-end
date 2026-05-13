package br.com.challenge.adapter.in.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.challenge.adapter.in.rest.response.ErrorResponse;
import br.com.challenge.domain.exception.CardAlreadyExistsException;
import br.com.challenge.domain.exception.CardNotFoundException;
import br.com.challenge.domain.exception.InvalidCardException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleInvalidCardException() {

        InvalidCardException exception =
                new InvalidCardException(
                        List.of("Invalid card")
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidCardException(
                        exception
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertEquals(
                "Invalid card",
                response.getBody()
                        .messages()
                        .get(0)
        );
    }

    @Test
    void shouldHandleCardNotFoundException() {

        CardNotFoundException exception =
                new CardNotFoundException();

        ResponseEntity<ErrorResponse> response =
                handler.handleCardNotFoundException(
                        exception
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertEquals(
                "Card not found",
                response.getBody()
                        .messages()
                        .get(0)
        );
    }

    @Test
    void shouldHandleCardAlreadyExistsException() {

        CardAlreadyExistsException exception =
                new CardAlreadyExistsException();

        ResponseEntity<ErrorResponse> response =
                handler.handleCardAlreadyExistsException(
                        exception
                );

        assertEquals(
                HttpStatus.CONFLICT,
                response.getStatusCode()
        );

        assertEquals(
                "Card already exists",
                response.getBody()
                        .messages()
                        .get(0)
        );
    }

    @Test
    void shouldHandleGenericException() {

        Exception exception =
                new RuntimeException(
                        "Unexpected error"
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleGenericException(
                        exception
                );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals(
                "An unexpected error occurred",
                response.getBody()
                        .messages()
                        .get(0)
        );
    }
}