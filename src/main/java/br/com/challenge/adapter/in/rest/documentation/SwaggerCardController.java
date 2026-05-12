package br.com.challenge.adapter.in.rest.documentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.challenge.adapter.in.rest.request.CreateCardRequest;
import br.com.challenge.adapter.in.rest.response.CreateCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cards", description = "Endpoints for managing cards")
public interface SwaggerCardController {
    
    @Operation(
        summary = "Create a new card", 
        description = "Creates a new card with the provided card number"
    )
    ResponseEntity<CreateCardResponse> create(
        @RequestBody CreateCardRequest request
    );
}
