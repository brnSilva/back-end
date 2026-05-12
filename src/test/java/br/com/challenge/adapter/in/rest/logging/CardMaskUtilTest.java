package br.com.challenge.adapter.in.rest.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMaskUtilTest {

    @Test
    void shouldMaskValidCardNumber() {
        String masked = CardMaskUtil.maskCardNumber("1234567812345678");

        assertEquals("1234********5678", masked);
    }

    @Test
    void shouldReturnInvalidWhenCardNumberIsNull() {
        assertEquals("INVALID_CARD_NUMBER", CardMaskUtil.maskCardNumber(null));
    }

    @Test
    void shouldReturnInvalidWhenCardNumberIsTooShort() {
        assertEquals("INVALID_CARD_NUMBER", CardMaskUtil.maskCardNumber("1234567"));
    }

    @Test
    void shouldMaskCardWithMoreThan16Digits() {

        String masked =
                CardMaskUtil.maskCardNumber(
                        "1234567890123456789"
                );

        assertEquals( "1234***********6789", masked );
    }
}