package br.com.challenge.adapter.in.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadErrorResponse(
    
    @Schema(example = "C1")
    String cardIdentifier,

    @Schema(example = "Card already exists")
    String message
) { }