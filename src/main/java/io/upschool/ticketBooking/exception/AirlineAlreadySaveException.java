package io.upschool.ticketBooking.exception;

public class AirlineAlreadySaveException extends RuntimeException {
    public AirlineAlreadySaveException(String message) {
        super(message);
    }

}
