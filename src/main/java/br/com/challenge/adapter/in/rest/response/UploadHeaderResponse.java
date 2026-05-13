package br.com.challenge.adapter.in.rest.response;

public record UploadHeaderResponse(

    String fileName,
    String fileDate,
    String lot,
    Integer expectedRecords
    
) {}