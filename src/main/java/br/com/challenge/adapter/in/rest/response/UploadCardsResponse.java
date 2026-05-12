package br.com.challenge.adapter.in.rest.response;

import java.util.List;

public record UploadCardsResponse(
    
    int processed,
    int success,
    int failed,
    List<UploadErrorResponse> errorDetails
) { }