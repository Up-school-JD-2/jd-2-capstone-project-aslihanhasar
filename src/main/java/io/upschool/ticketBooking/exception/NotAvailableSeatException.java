package io.upschool.ticketBooking.exception;

public class NotAvailableSeatException extends RuntimeException {
    public NotAvailableSeatException(String message) {
        super(message);
    }

}
