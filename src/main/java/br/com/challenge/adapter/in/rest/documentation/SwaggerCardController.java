package br.com.challenge.adapter.in.rest.documentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import br.com.challenge.adapter.in.rest.request.CreateCardRequest;
import br.com.challenge.adapter.in.rest.response.CreateCardResponse;
import br.com.challenge.adapter.in.rest.response.ErrorResponse;
import br.com.challenge.adapter.in.rest.response.FindCardResponse;
import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cards", description = "Endpoints for managing cards")
public interface SwaggerCardController {
    
    @Operation(
        summary = "Create a new card",
        description = "Creates a new card with the provided card number",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Card created successfully",
            content = @Content(schema = @Schema(implementation = CreateCardResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid card number",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict - Card already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<CreateCardResponse> create(
        @RequestBody CreateCardRequest request
    );

    @Operation(
        summary = "Find card by cardNumber",
        description = "Retrieves the ID of a card using its cardNumber",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Card found successfully",
            content = @Content(schema = @Schema(implementation = FindCardResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid card number format",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Card not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    FindCardResponse findByCardNumber(
        @PathVariable @Schema(example = "1234567812345678") String cardNumber
    );

    @Operation(
        summary = "Upload multiple cards",
        description = "Uploads a TXT file containing multiple card numbers for batch processing",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "File processed successfully",
            content = @Content(schema = @Schema(implementation = UploadCardsResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file format or content",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Authentication required",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during file processing",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    UploadCardsResponse upload(
        @RequestPart("file") MultipartFile file
    );
}