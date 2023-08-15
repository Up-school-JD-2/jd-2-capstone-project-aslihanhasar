package io.upschool.ticketBooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteSaveRequest {
    @NotNull
    private Long departureAirportId;
    @NotNull
    private Long arrivalAirportId;
    @NotBlank
    private String departureDate;
    @NotBlank
    private String departureTime;
    @NotBlank
    private String arrivalDate;
    @NotBlank
    private String arrivalTime;
}
