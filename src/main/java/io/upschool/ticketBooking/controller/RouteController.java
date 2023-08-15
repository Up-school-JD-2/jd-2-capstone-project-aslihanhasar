package io.upschool.ticketBooking.controller;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.dto.request.RouteSaveRequest;
import io.upschool.ticketBooking.dto.response.RouteSaveResponse;
import io.upschool.ticketBooking.dto.response.RouteSearchResponse;
import io.upschool.ticketBooking.service.BaseResponseService;
import io.upschool.ticketBooking.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;
    private final BaseResponseService baseResponseService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<RouteSearchResponse>>> getAllRoutes(
            @RequestParam(defaultValue = "") String departureKey,
            @RequestParam(defaultValue = "") String arrivalKey) {
        List<RouteSearchResponse> routes = routeService.getAllRoutes(departureKey, arrivalKey);
        return baseResponseService.createSuccessResponseList(routes);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<RouteSaveResponse>> createRoute(
            @Valid @RequestBody RouteSaveRequest request) {
        RouteSaveResponse routeSaveResponse = routeService.save(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, routeSaveResponse);
    }
}
