package br.com.challenge.domain.exception;

public class CardAlreadyExistsException extends RuntimeException {
    
    public CardAlreadyExistsException() {
        super("Card already exists");
    }
}