package io.upschool.ticketBooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAirlineToAirportRequest {
    private Long airportId;
    private Set<Long> airlineIds;
}
