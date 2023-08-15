package io.upschool.ticketBooking.utils;

import io.upschool.ticketBooking.exception.InvalidCreditCartNumberException;

public class CreditCardUtils {
    public static String maskCreditCardNumber(String cardNumber) {
        String cleanedCardNumber = cardNumber.replaceAll("[\\s\\-,]", "");
        if (!cleanedCardNumber.matches("\\d+")) {
            throw new InvalidCreditCartNumberException("Credit card number must contain only digits");
        }
        if (cleanedCardNumber.length()>16) {
            throw new InvalidCreditCartNumberException("Invalid card number");
        }
        return cleanedCardNumber.substring(0, 6) + "******" +
                cleanedCardNumber.substring(cleanedCardNumber.length() - 4);
    }
}
