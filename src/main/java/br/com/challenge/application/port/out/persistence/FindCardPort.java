package br.com.challenge.application.port.out.persistence;

import java.util.Optional;

import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

public interface FindCardPort {
    
    Optional<Card> findByCardNumber(CardNumber cardNumber);//TODO: This needs to change in the next steps
}