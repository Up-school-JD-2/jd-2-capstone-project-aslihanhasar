package io.upschool.ticketBooking.exception;

public class RouteValidationException extends RuntimeException{
    public RouteValidationException(String message) {
        super(message);
    }

    public RouteValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
