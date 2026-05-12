package br.com.challenge.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.domain.model.entity.Card;

@Service
public class CreateCardService implements CreateCardUseCase {

    private final SaveCardPort saveCardPort;

    public CreateCardService(SaveCardPort saveCardPort) {
        this.saveCardPort = saveCardPort;
    }

    @Override
    public Card execute(CreateCardCommand command) {
        Card card = new Card(
            null,
            command.cardNumber(),
            LocalDateTime.now()
        );

        return saveCardPort.save(card);
    }
    
}
