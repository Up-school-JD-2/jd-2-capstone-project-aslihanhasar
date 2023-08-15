package io.upschool.ticketBooking.enums;

import io.upschool.ticketBooking.exception.InvalidTicketClassException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum TicketClass {
    FIRST_CLASS("First Class"),
    ECONOMY_CLASS("Economy Class"),
    BUSINESS_CLASS("Business Class");

    private final String value;
    TicketClass(String value) {
        this.value = value;
    }
    public static TicketClass fromValue(String value) {
        for (TicketClass ticketClass : values()) {
            if (ticketClass.value.equalsIgnoreCase(value)) {
                return ticketClass;
            }
        }
        throw new InvalidTicketClassException("Unsupported ticket class: " + value);
    }
}
