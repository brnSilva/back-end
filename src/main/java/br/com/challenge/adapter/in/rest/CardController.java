package br.com.challenge.adapter.in.rest;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.challenge.adapter.in.rest.documentation.SwaggerCardController;
import br.com.challenge.adapter.in.rest.request.CreateCardRequest;
import br.com.challenge.adapter.in.rest.response.CreateCardResponse;
import br.com.challenge.adapter.in.rest.response.FindCardResponse;
import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.in.FindCardUseCase;
import br.com.challenge.application.port.in.UploadCardsUseCase;
import br.com.challenge.domain.model.entity.Card;



@RestController
@RequestMapping("/cards")
public class CardController implements SwaggerCardController {

    private final CreateCardUseCase createCardUseCase;

    private final FindCardUseCase findCardUseCase;

    private final UploadCardsUseCase uploadCardsUseCase;

    public CardController(CreateCardUseCase createCardUseCase, FindCardUseCase findCardUseCase, UploadCardsUseCase uploadCardsUseCase) {
        this.createCardUseCase = createCardUseCase;
        this.findCardUseCase = findCardUseCase;
        this.uploadCardsUseCase = uploadCardsUseCase;
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

    @GetMapping("/{cardNumber}")
    public FindCardResponse findByCardNumber(
                @PathVariable String cardNumber
    ) {
        UUID uuid = findCardUseCase.execute(
            cardNumber
        );

        return new FindCardResponse(uuid);
    }

    @PostMapping("/upload")
    public UploadCardsResponse upload(
                @RequestParam("file")
                MultipartFile file
    ) {
        return uploadCardsUseCase.execute(file);
    }
}