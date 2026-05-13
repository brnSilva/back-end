package br.com.challenge.adapter.in.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadTrailerResponse(
    
    @Schema(example = "LOTE0001")
    String lot,

    @Schema(example = "100")
    Integer expectedRecords
    
) { }
