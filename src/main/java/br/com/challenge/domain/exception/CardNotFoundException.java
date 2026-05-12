package br.com.challenge.domain.exception;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException() {
        super("Card not found");
    }
}