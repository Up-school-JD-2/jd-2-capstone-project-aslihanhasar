package io.upschool.ticketBooking.exception;

public class InvalidDateTimeFormatException extends RuntimeException{
    public InvalidDateTimeFormatException(String message) {
        super(message);
    }

    public InvalidDateTimeFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
