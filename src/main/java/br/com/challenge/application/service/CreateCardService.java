package br.com.challenge.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.application.port.out.security.EncryptDataPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Service
public class CreateCardService implements CreateCardUseCase {

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

        String encrypted =
                encryptDataPort.encrypt(cardNumber.value());

        Card card = new Card(
                        null,
                        encrypted,
                        hash,
                        LocalDateTime.now()
                    );

        return saveCardPort.save(card);
    }
}