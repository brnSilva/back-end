package br.com.challenge.adapter.in.rest.logging;

public final class CardMaskUtil {
    
    private CardMaskUtil() { }

    public static String maskCardNumber(String cardNumber) {

        if (cardNumber == null || cardNumber.length() < 8) {
            return "INVALID_CARD_NUMBER";
        }

        int maskLength = cardNumber.length() - 8;

        return cardNumber.substring(0,4)
                            + "*".repeat(maskLength)
                            + cardNumber.substring(cardNumber.length() - 4);    
    }
}