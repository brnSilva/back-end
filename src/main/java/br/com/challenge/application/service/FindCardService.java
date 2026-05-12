package br.com.challenge.application.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.challenge.adapter.in.rest.logging.CardMaskUtil;
import br.com.challenge.application.port.in.FindCardUseCase;
import br.com.challenge.application.port.out.persistence.FindCardPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.exception.CardNotFoundException;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Service
public class FindCardService implements FindCardUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(FindCardService.class);

    private final FindCardPort findCardPort;

    private final HashDataPort hashDataPort;

    public FindCardService(FindCardPort findCardPort, HashDataPort hashDataPort) {

        this.findCardPort = findCardPort;
        this.hashDataPort = hashDataPort;
    }

    @Override
    public UUID execute(String cardNumber) {

        LOGGER.info("Card lookup requested - card={}", CardMaskUtil.maskCardNumber(cardNumber));

        CardNumber cardNumberValidated = new CardNumber(cardNumber);

        String hash = hashDataPort.hash(cardNumberValidated.value());

        Card card = findCardPort.findByHashCardNumber(hash)
                                    .orElseThrow(() -> {

                                        LOGGER.info("Card not found - card={}", 
                                                CardMaskUtil.maskCardNumber(cardNumber));

                                        return new CardNotFoundException();
                                    });

        LOGGER.info("Card lookup successfully - id={}", card.id());
        return card.id();
    }
}