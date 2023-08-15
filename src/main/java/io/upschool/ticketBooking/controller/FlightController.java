package io.upschool.ticketBooking.controller;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.dto.request.FlightSaveRequest;
import io.upschool.ticketBooking.dto.response.FlightSaveResponse;
import io.upschool.ticketBooking.dto.response.FlightSearchResponse;
import io.upschool.ticketBooking.service.BaseResponseService;
import io.upschool.ticketBooking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;
    private final BaseResponseService baseResponseService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<FlightSearchResponse>>> getAllFlights(
            @RequestParam(defaultValue = "") String departureKey,
            @RequestParam(defaultValue = "") String arrivalKey,
            @RequestParam (required = false)String departureDate ) {
        List<FlightSearchResponse> flights = flightService.getAllFlights(departureKey, arrivalKey, departureDate);
        return baseResponseService.createSuccessResponseList(flights);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<FlightSaveResponse>> createFlight(
            @Valid @RequestBody FlightSaveRequest request) {
        FlightSaveResponse flightSaveResponse = flightService.save(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, flightSaveResponse);
    }
}
