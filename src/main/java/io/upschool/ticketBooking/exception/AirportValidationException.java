package io.upschool.ticketBooking.exception;

public class AirportValidationException extends RuntimeException{
    public AirportValidationException(String message) {
        super(message);
    }

    public AirportValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
