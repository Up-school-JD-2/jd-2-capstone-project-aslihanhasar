package io.upschool.ticketBooking.exception;

public class AirlineAlreadySaveException extends RuntimeException{
    public AirlineAlreadySaveException(String message) {
        super(message);
    }

    public AirlineAlreadySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
