package io.upschool.ticketBooking.exception;

public class InvalidTicketClassException extends RuntimeException{
    public InvalidTicketClassException(String message) {
        super(message);
    }

    public InvalidTicketClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
