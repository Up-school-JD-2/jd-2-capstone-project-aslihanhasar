package io.upschool.ticketBooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSaveResponse {
    private Long flightId;
    private RouteSaveResponse route;
    private AirlineSaveResponse airline;
    private int capacity;
}
