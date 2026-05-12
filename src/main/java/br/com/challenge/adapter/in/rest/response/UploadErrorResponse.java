package br.com.challenge.adapter.in.rest.response;

public record UploadErrorResponse(
    
    String cardIdentifier,
    String message
) { }