package br.com.challenge.application.port.in;

import java.util.UUID;

public interface FindCardUseCase {
    
    UUID execute(String cardNumber);
}