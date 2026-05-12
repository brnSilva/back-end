package br.com.challenge.domain.model.vo;

import br.com.challenge.domain.exception.InvalidCardException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNumberTest {

    @Test
    void shouldCreateCardNumberWhenValueIsValid() {
        CardNumber cardNumber = new CardNumber("1234123412341234");

        assertEquals("1234123412341234", cardNumber.value());
    }

    @Test
    void shouldThrowWhenCardNumberIsNull() {
        InvalidCardException exception = assertThrows(
                InvalidCardException.class,
                () -> new CardNumber(null)
        );

        assertTrue(exception.getErrors().contains("Card number cannot be null or blank"));
    }

    @Test
    void shouldThrowWhenCardNumberContainsNonDigits() {
        InvalidCardException exception = assertThrows(
                InvalidCardException.class,
                () -> new CardNumber("1234X23412341234")
        );

        assertTrue(exception.getErrors().contains("Card number must contain only digits"));
    }

    @Test
    void shouldThrowWhenCardNumberLengthIsInvalid() {
        InvalidCardException exception = assertThrows(
                InvalidCardException.class,
                () -> new CardNumber("123456789012345")
        );

        assertTrue(exception.getErrors().contains("Card number must be exactly 16 digits"));
    }

    @Test
    void shouldThrowWhenCardNumberIsBlank() {

        InvalidCardException exception =
                assertThrows(
                        InvalidCardException.class,
                        () -> new CardNumber(" ")
                );

        assertTrue(
                exception.getErrors()
                        .contains(
                            "Card number cannot be null or blank"
                        )
        );
    }

    @Test
    void shouldThrowWhenCardNumberHasMoreThan16Digits() {

        InvalidCardException exception =
                assertThrows(
                        InvalidCardException.class,
                        () -> new CardNumber(
                                "1234123412341234123"
                        )
                );

        assertTrue(
                exception.getErrors()
                        .contains(
                            "Card number must be exactly 16 digits"
                        )
        );
    }
}