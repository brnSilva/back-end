package br.com.challenge.adapter.in.rest.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadCardsRequest(
    
    @Schema(
        type = "string",
        format = "binary"
    )

    MultipartFile file

) { }