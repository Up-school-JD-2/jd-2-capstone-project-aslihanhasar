package io.upschool.ticketBooking.exception;

public class AirlineNotFoundException extends RuntimeException{
    public AirlineNotFoundException(String message) {
        super(message);
    }

    public AirlineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
