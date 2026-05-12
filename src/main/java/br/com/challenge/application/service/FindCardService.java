package br.com.challenge.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.challenge.application.port.in.FindCardUseCase;
import br.com.challenge.application.port.out.persistence.FindCardPort;
import br.com.challenge.domain.exception.CardNotFoundException;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Service
public class FindCardService implements FindCardUseCase {

    private final FindCardPort findCardPort;

    public FindCardService(FindCardPort findCardPort) {

        this.findCardPort = findCardPort;
    }

    @Override
    public UUID execute(String cardNumber) {

        Card card = findCardPort.findByCardNumber(new CardNumber(cardNumber))
                .orElseThrow(CardNotFoundException::new);

        return card.id();
    }
}