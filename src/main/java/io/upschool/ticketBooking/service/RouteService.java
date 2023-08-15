package io.upschool.ticketBooking.service;

import io.upschool.ticketBooking.dto.request.RouteSaveRequest;
import io.upschool.ticketBooking.dto.response.*;
import io.upschool.ticketBooking.entity.Airport;
import io.upschool.ticketBooking.entity.Route;
import io.upschool.ticketBooking.exception.RouteNotFoundException;
import io.upschool.ticketBooking.exception.RouteValidationException;
import io.upschool.ticketBooking.repository.RouteRepository;
import io.upschool.ticketBooking.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final AirportService airportService;

    public RouteSaveResponse save(RouteSaveRequest request) {
        validateRouteSaveRequest(request);
        airportService.checkIsAirportExist(request.getDepartureAirportId());
        airportService.checkIsAirportExist(request.getArrivalAirportId());
        checkRouteIsAlreadySaved(request);
        Route savedRoute = buildRouteAndSave(request);
        return convertRouteToResponse(savedRoute);
    }

    public List<RouteSearchResponse> getAllRoutes(String departureKey, String arrivalKey) {
        List<Route> routes;
        if (departureKey.isEmpty() && arrivalKey.isEmpty()) {
            routes = routeRepository.findAll();
        } else {
            routes = findRoutesByDepartureAndArrival(departureKey, arrivalKey);
        }
        if (routes.isEmpty()) {
            throw new RouteNotFoundException("No routes found matching the search criteria.");
        }
        return routes.stream()
                .map(this::convertRouteToSearchResponse)
                .toList();
    }

    protected Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Route not found."));
    }

    protected void checkRouteIsAlreadySaved(RouteSaveRequest request) {
        LocalDate departureDate = DateUtils.parseLocalDate(request.getDepartureDate());
        LocalTime departureTime = DateUtils.parseLocalTime(request.getDepartureTime());
        boolean routeExists = routeRepository.
                existsByDepartureDateAndDepartureTimeAndDepartureAirportIdAndArrivalAirportId(
                        departureDate, departureTime,
                        request.getDepartureAirportId(), request.getArrivalAirportId());
        if (routeExists) {
            throw new RouteValidationException("Route already exists.");
        }
    }

    protected void checkIsRouteExist(Long routeId) {
        boolean existRoute = routeRepository.existsById(routeId);
        if (!existRoute) {
            throw new RouteNotFoundException("Route not found.");
        }
    }

    protected RouteSaveResponse convertRouteToResponse(Route route) {
        Airport departureAirport = route.getDepartureAirport();
        Airport arrivalAirport = route.getArrivalAirport();
        LocalDateTime departureDateTime = DateUtils.combineDateAndTime
                (route.getDepartureDate(), route.getDepartureTime());
        LocalDateTime arrivalDateTime = DateUtils.combineDateAndTime
                (route.getArrivalDate(), route.getArrivalTime());
        return RouteSaveResponse.builder()
                .flightRouteId(route.getId())
                .departureAirport(airportService.convertAirportToResponse(departureAirport))
                .arrivalAirport(airportService.convertAirportToResponse(arrivalAirport))
                .departureDateTime(departureDateTime)
                .arrivalDateTime(arrivalDateTime)
                .build();
    }

    private List<Route> findRoutesByDepartureAndArrival(String departureKey, String arrivalKey) {
        return routeRepository.findByDepartureAirport_AirportLocationIgnoreCaseOrArrivalAirport_AirportLocationIgnoreCase(
                departureKey, arrivalKey);
    }

    private RouteSearchResponse convertRouteToSearchResponse(Route route) {
        AirportSaveResponse departureAirport = airportService
                .convertAirportToResponse(route.getDepartureAirport());
        AirportSaveResponse arrivalAirport = airportService
                .convertAirportToResponse(route.getArrivalAirport());
        return RouteSearchResponse.builder()
                .flightRouteId(route.getId())
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .build();
    }

    private Route buildRouteAndSave(RouteSaveRequest request) {
        Airport departureAirport = airportService.getAirportById(request.getDepartureAirportId());
        Airport arrivalAirport = airportService.getAirportById(request.getArrivalAirportId());
        validateRouteLocations(departureAirport, arrivalAirport);
        LocalDate departureDate = DateUtils.parseLocalDate(request.getDepartureDate());
        LocalTime departureTime = DateUtils.parseLocalTime(request.getDepartureTime());
        LocalDate arrivalDate = DateUtils.parseLocalDate(request.getArrivalDate());
        LocalTime arrivalTime = DateUtils.parseLocalTime(request.getArrivalTime());
        validateDepartureAndArrivalDateTime(departureDate, departureTime, arrivalDate, arrivalTime);
        Route route = Route.builder()
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureDate(departureDate)
                .departureTime(departureTime)
                .arrivalDate(arrivalDate)
                .arrivalTime(arrivalTime)
                .build();
        return routeRepository.save(route);
    }

    private void validateRouteSaveRequest(RouteSaveRequest request) {
        boolean anyFieldBlank = Stream.of(request.getArrivalAirportId(),
                        request.getDepartureAirportId(),
                        request.getDepartureDate(),
                        request.getArrivalDate(),
                        request.getDepartureTime(),
                        request.getArrivalTime())
                .anyMatch(value -> value == null || value.toString().trim().isEmpty());
        if (anyFieldBlank) {
            throw new RouteValidationException("Required fields cannot be left blank");
        }
    }

    private void validateRouteLocations(Airport departureAirport, Airport arrivalAirport) {
        String departureLocation = departureAirport.getAirportLocation();
        String arrivalLocation = arrivalAirport.getAirportLocation();
        boolean sameLocation = departureLocation.equalsIgnoreCase(arrivalLocation);
        if (sameLocation) {
            throw new RouteValidationException("Departure and arrival airports cannot be in the same location.");
        }
    }

    private void validateDepartureAndArrivalDateTime(LocalDate departureDate, LocalTime departureTime,
                                                     LocalDate arrivalDate, LocalTime arrivalTime) {
        if (departureDate.isEqual(arrivalDate) && departureTime.isAfter(arrivalTime)) {
            throw new RouteValidationException("Departure time cannot be after arrival time.");
        }
    }
}
