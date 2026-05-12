package br.com.challenge.domain.exception;

import java.util.List;

public class InvalidCardException extends RuntimeException {

    private final List<String> errors;

    public InvalidCardException(List<String> errors) {
        super("Invalid card");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}