package io.upschool.ticketBooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportSaveRequest {
    @NotBlank
    private String airportName;
    @NotBlank
    private String airportCode;
    @NotBlank
    private String airportLocation;
}
