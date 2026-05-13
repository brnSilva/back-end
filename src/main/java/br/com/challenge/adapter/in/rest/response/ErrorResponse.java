package br.com.challenge.adapter.in.rest.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    int status,
    List<String> messages,
    LocalDateTime timestamp
) {
}