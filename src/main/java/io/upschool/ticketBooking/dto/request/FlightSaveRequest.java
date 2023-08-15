package io.upschool.ticketBooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSaveRequest {
    @NotNull
    private Long routeId;
    @NotNull
    private Long airlineId;
    @NotNull
    @Min(15)
    private int capacity;
    @NotNull
    private BigDecimal ticketBasePrice;
}
