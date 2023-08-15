package io.upschool.ticketBooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteSearchResponse {
    private Long flightRouteId;
    private AirportSaveResponse departureAirport;
    private AirportSaveResponse arrivalAirport;
}
