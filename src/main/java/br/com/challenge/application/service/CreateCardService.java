package br.com.challenge.application.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.challenge.adapter.in.rest.logging.CardMaskUtil;
import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.application.port.out.security.EncryptDataPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.exception.CardAlreadyExistsException;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Service
public class CreateCardService implements CreateCardUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCardService.class);

    private final SaveCardPort saveCardPort;
    private final HashDataPort hashDataPort;
    private final EncryptDataPort encryptDataPort;

    public CreateCardService(SaveCardPort saveCardPort, HashDataPort hashDataPort, EncryptDataPort encryptDataPort) {
        this.saveCardPort = saveCardPort;
        this.hashDataPort = hashDataPort;
        this.encryptDataPort = encryptDataPort;
    }

    @Override
    public Card execute(CreateCardCommand command) {

        CardNumber cardNumber = new CardNumber(command.cardNumber());
        
        String hash = hashDataPort.hash(cardNumber.value());

        if(saveCardPort.existsByHashCardNumber(hash)){
            LOGGER.info("Card already exists - card={}", CardMaskUtil.maskCardNumber(cardNumber.value()));
            throw new CardAlreadyExistsException();
        }

        String encrypted =
                encryptDataPort.encrypt(cardNumber.value());

        Card card = new Card(
                        null,
                        encrypted,
                        hash,
                        LocalDateTime.now()
                    );

        try {
            Card savedCard = saveCardPort.save(card);

            LOGGER.info( "Card created successfully - id={}", savedCard.id());

            return savedCard;
        } catch (DataIntegrityViolationException e) {
            throw new CardAlreadyExistsException();
        }
    }
}