package io.upschool.ticketBooking.exception;

public class RouteAlreadySaveException extends RuntimeException{
    public RouteAlreadySaveException(String message) {
        super(message);
    }

    public RouteAlreadySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
