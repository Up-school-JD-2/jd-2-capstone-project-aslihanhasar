package io.upschool.ticketBooking.controller;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.dto.request.AirlineSaveRequest;
import io.upschool.ticketBooking.dto.response.*;
import io.upschool.ticketBooking.service.AirlineService;
import io.upschool.ticketBooking.service.BaseResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;
    private final BaseResponseService baseResponseService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<AirlineSaveResponse>>> getAllAirlines(
            @RequestParam(defaultValue = "") String searchKey) {
        List<AirlineSaveResponse> airlines = airlineService.getAllAirlines(searchKey);
        return baseResponseService.createSuccessResponseList(airlines);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<AirlineSaveResponse>> createAirline(
            @Valid @RequestBody AirlineSaveRequest request) {
        AirlineSaveResponse airlineSaveResponse = airlineService.save(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, airlineSaveResponse);
    }
}



