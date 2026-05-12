package br.com.challenge.domain.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.challenge.domain.model.vo.CardNumber;

public record Card(
    UUID id,
    CardNumber cardNumber,
    LocalDateTime createdAt
) {}