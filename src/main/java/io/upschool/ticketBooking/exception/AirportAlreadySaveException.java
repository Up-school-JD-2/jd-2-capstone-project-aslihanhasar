package io.upschool.ticketBooking.exception;

public class AirportAlreadySaveException extends RuntimeException{
    public AirportAlreadySaveException(String message) {
        super(message);
    }

    public AirportAlreadySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
