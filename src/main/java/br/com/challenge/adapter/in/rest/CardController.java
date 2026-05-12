package br.com.challenge.adapter.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.challenge.adapter.in.rest.documentation.SwaggerCardController;
import br.com.challenge.adapter.in.rest.request.CreateCardRequest;
import br.com.challenge.adapter.in.rest.response.CreateCardResponse;
import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.domain.model.entity.Card;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/cards")
public class CardController implements SwaggerCardController {

    private final CreateCardUseCase createCardUseCase;

    public CardController(CreateCardUseCase createCardUseCase) {
        this.createCardUseCase = createCardUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<CreateCardResponse> create(
        @RequestBody CreateCardRequest request
    ) {
        Card card = createCardUseCase.execute(
            new CreateCardCommand(
                request.cardNumber()
            )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateCardResponse(card.id()));
    }
}
