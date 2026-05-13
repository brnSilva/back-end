package br.com.challenge.adapter.in.rest.response;

import java.util.List;

public record UploadCardsResponse(
    
    UploadHeaderResponse header,
    UploadTrailerResponse trailer,
    Boolean lotConsistent,
    Boolean quantityConsistent,
    int processed,
    int success,
    int failed,
    List<UploadErrorResponse> errorDetails
    
) { }