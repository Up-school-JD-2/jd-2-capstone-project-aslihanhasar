package io.upschool.ticketBooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportSaveResponse {
    private Long airportId;
    private String airport;
    private String airportLocation;
}
