package br.com.challenge.application.port.out.persistence;

import java.util.Optional;

import br.com.challenge.domain.model.entity.Card;

public interface FindCardPort {
    
    Optional<Card> findByHashCardNumber(String hashCardNumber);
}