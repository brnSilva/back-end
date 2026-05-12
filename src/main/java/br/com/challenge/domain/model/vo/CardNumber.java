package br.com.challenge.domain.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.challenge.domain.exception.InvalidCardException;

public record CardNumber(
    String value
) {
    public CardNumber {
        validate(value);
    }

    private void validate(String value) {

        List<String> errors = new ArrayList<>();
        
        if (value == null || value.isBlank()) {
            errors.add("Card number cannot be null or blank");
        }

        if (!value.matches("\\d+")) {
            errors.add("Card number must contain only digits");
        }

        if (!value.matches("\\d{16}")) {
            errors.add("Card number must be exactly 16 digits");
        }

        if (!errors.isEmpty()) {
            throw new InvalidCardException(errors);
        }
    }
}
