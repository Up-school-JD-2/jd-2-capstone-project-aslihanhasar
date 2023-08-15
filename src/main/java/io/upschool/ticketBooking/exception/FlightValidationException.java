package io.upschool.ticketBooking.exception;

public class FlightValidationException extends RuntimeException{
    public FlightValidationException(String message) {
        super(message);
    }

    public FlightValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
