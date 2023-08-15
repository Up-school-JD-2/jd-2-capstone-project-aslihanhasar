package io.upschool.ticketBooking.exception;

public class AirlineValidationException extends RuntimeException{
    public AirlineValidationException(String message) {
        super(message);
    }

    public AirlineValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
