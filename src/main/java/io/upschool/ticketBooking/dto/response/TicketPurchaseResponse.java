package io.upschool.ticketBooking.dto.response;

import io.upschool.ticketBooking.enums.TicketClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketPurchaseResponse {
    private String ticketNumber;
    private String passengerName;
    private FlightSaveResponse flightSaveResponse;
    private TicketClass ticketClass;
    private String maskedCreditCardNumber;
    private BigDecimal totalPrice;
}
