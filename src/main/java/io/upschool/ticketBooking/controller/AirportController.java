package io.upschool.ticketBooking.controller;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.dto.request.AddAirlineToAirportRequest;
import io.upschool.ticketBooking.dto.request.AirlineSaveRequest;
import io.upschool.ticketBooking.dto.request.AirportSaveRequest;
import io.upschool.ticketBooking.dto.response.*;
import io.upschool.ticketBooking.entity.Airport;
import io.upschool.ticketBooking.entity.Ticket;
import io.upschool.ticketBooking.service.AirportService;
import io.upschool.ticketBooking.service.BaseResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;
    private final BaseResponseService baseResponseService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<AirportSaveResponse>>> getAllAirports(
            @RequestParam(defaultValue = "") String searchKey) {
        List<AirportSaveResponse> airports = airportService.getAllAirports(searchKey);
        return baseResponseService.createSuccessResponseList(airports);
    }

    @GetMapping("/{airportId}")
    public ResponseEntity<BaseResponse<AirportDetailResponse>> getAirportDetails
            (@PathVariable("airportId") Long airportId) {
        AirportDetailResponse airportDetailResponse = airportService.getAirportDetails(airportId);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, airportDetailResponse);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<AirportSaveResponse>> createAirport(
            @Valid @RequestBody AirportSaveRequest request) {
        AirportSaveResponse airportSaveResponse = airportService.save(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, airportSaveResponse);
    }

    @PostMapping("/add-airline")
    public ResponseEntity<BaseResponse<AddAirlineToAirportResponse>> addAirlineToAirport(
            @RequestBody AddAirlineToAirportRequest request) {
        AddAirlineToAirportResponse airportSaveResponse = airportService.addAirlineToAirport(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, airportSaveResponse);
    }

}
