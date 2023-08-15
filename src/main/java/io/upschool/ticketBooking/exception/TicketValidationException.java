package io.upschool.ticketBooking.exception;

public class TicketValidationException extends RuntimeException{
    public TicketValidationException(String message) {
        super(message);
    }

    public TicketValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
