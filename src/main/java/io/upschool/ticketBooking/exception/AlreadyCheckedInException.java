package io.upschool.ticketBooking.exception;

public class AlreadyCheckedInException extends RuntimeException{
    public AlreadyCheckedInException(String message) {
        super(message);
    }

    public AlreadyCheckedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
