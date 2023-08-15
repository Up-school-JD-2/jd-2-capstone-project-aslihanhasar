package io.upschool.ticketBooking.exception;

public class InvalidCreditCartNumberException extends RuntimeException{
    public InvalidCreditCartNumberException(String message) {
        super(message);
    }

    public InvalidCreditCartNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
