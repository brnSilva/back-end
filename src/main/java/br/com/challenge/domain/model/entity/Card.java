package br.com.challenge.domain.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record Card(
    UUID id,
    String cardNumber,
    LocalDateTime createdAt
) {}