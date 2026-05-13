package br.com.challenge.application.port.in;

import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.domain.model.entity.Card;

public interface CreateCardUseCase {

    Card execute(CreateCardCommand command);
}
