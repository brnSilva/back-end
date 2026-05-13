package br.com.challenge.adapter.in.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadHeaderResponse(

    @Schema(example = "CARD-CHALLENGE-TXT")
    String fileName,

    @Schema(example = "2024-06-01")
    String fileDate,
    
    @Schema(example = "LOTE0001")
    String lot,

    @Schema(example = "100")
    Integer expectedRecords
    
) {}