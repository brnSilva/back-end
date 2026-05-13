package br.com.challenge.adapter.in.rest.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadCardsResponse(
    
    UploadHeaderResponse header,
    
    UploadTrailerResponse trailer,

    Boolean lotConsistent,

    Boolean quantityConsistent,

    @Schema(example = "100")
    int processed,

    @Schema(example = "90")
    int success,

    @Schema(example = "10")
    int failed,

    List<UploadErrorResponse> errorDetails
    
) { }