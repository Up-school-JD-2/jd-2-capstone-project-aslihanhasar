package io.upschool.ticketBooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AirportDetailResponse {
    private AirportSaveResponse airport;
    private List<AirlineSaveResponse> airlines;
}
