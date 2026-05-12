package br.com.challenge.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.challenge.application.port.in.FindCardUseCase;
import br.com.challenge.application.port.out.persistence.FindCardPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.exception.CardNotFoundException;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Service
public class FindCardService implements FindCardUseCase {

    private final FindCardPort findCardPort;

    private final HashDataPort hashDataPort;

    public FindCardService(FindCardPort findCardPort, HashDataPort hashDataPort) {

        this.findCardPort = findCardPort;
        this.hashDataPort = hashDataPort;
    }

    @Override
    public UUID execute(String cardNumber) {

        CardNumber cardNumberValidated = new CardNumber(cardNumber);

        String hash = hashDataPort.hash(cardNumberValidated.value());

        Card card = findCardPort.findByHashCardNumber(hash)
                        .orElseThrow(CardNotFoundException::new);

        return card.id();
    }
}