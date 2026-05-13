package br.com.challenge.adapter.in.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCardRequest(

        @Schema( example = "1234567812345678" )
        String cardNumber
) { }