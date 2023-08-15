package io.upschool.ticketBooking.exception;

public class FlightAlreadySaveException extends RuntimeException {
    public FlightAlreadySaveException(String message) {
        super(message);
    }

}
