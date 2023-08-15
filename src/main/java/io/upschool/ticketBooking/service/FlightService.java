package io.upschool.ticketBooking.service;

import io.upschool.ticketBooking.dto.request.FlightSaveRequest;
import io.upschool.ticketBooking.dto.response.FlightSaveResponse;
import io.upschool.ticketBooking.dto.response.FlightSearchResponse;
import io.upschool.ticketBooking.dto.response.RouteSaveResponse;
import io.upschool.ticketBooking.entity.Airline;
import io.upschool.ticketBooking.entity.Flight;
import io.upschool.ticketBooking.entity.Route;
import io.upschool.ticketBooking.exception.*;
import io.upschool.ticketBooking.repository.FlightRepository;
import io.upschool.ticketBooking.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final RouteService routeService;
    private final AirlineService airlineService;

    public FlightSaveResponse save(FlightSaveRequest request) {
        validateFlightSaveRequest(request);
        checkRouteAndAirlineExistence(request);
        Flight savedFlight = buildFlightAndSave(request);
        return convertFlightToResponse(savedFlight);
    }

    public List<FlightSearchResponse> getAllFlights(String departureKey,
                                                    String arrivalKey,
                                                    String departureDate) {
        List<Flight> flights;
        if (departureKey.isEmpty() && arrivalKey.isEmpty()) {
            flights = flightRepository.findAll();
        } else {
            LocalDate localDepartureDate = DateUtils.parseLocalDate(departureDate);
            flights = flightRepository.
                    findByRoute_DepartureAirport_AirportLocationContainingIgnoreCaseAndRoute_ArrivalAirport_AirportLocationContainingIgnoreCaseAndRoute_DepartureDate
                            (departureKey, arrivalKey, localDepartureDate);
        }
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("Flight not found.");
        }
        return flights.stream()
                .map(this::convertFlightToSearchResponse)
                .toList();
    }

    protected void reserveSeats(Flight flight, int requestedSeats) {
        boolean availableSeats = isAvailableSeats(flight, requestedSeats);
        if (!availableSeats) {
            throw new NotAvailableSeatException("Not enough available seats.");
        }
        int remainingSeats = flight.getRemainingSeats();
        flight.setRemainingSeats(remainingSeats - requestedSeats);
        flightRepository.save(flight);
    }

    protected void updateAvailableSeats(Flight flight, int requestedSeats) {
        int currentAvailableSeats = flight.getRemainingSeats();
        int updatedAvailableSeats = currentAvailableSeats + requestedSeats;
        flight.setRemainingSeats(updatedAvailableSeats);
        flightRepository.save(flight);
    }

    protected Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found."));
    }

    protected FlightSaveResponse convertFlightToResponse(Flight flight) {
        return FlightSaveResponse
                .builder()
                .flightId(flight.getId())
                .capacity(flight.getCapacity())
                .route(routeService.convertRouteToResponse(flight.getRoute()))
                .airline(airlineService.convertAirlineToResponse(flight.getAirline()))
                .build();
    }

    private FlightSearchResponse convertFlightToSearchResponse(Flight flight) {
        RouteSaveResponse routeSaveResponse = routeService.convertRouteToResponse(flight.getRoute());
        return FlightSearchResponse
                .builder()
                .flightId(flight.getId())
                .routeSaveResponse(routeSaveResponse)
                .airline(flight.getAirline().getAirlineName() + " - " + flight.getAirline().getAirlineCode())
                .build();
    }

    private Flight buildFlightAndSave(FlightSaveRequest request) {
        checkIsFlightAlreadySaved(request);
        Airline airline = airlineService.getAirlineById(request.getAirlineId());
        Route route = routeService.getRouteById(request.getRouteId());
        Flight flight = Flight
                .builder()
                .airline(airline)
                .route(route)
                .capacity(request.getCapacity())
                .remainingSeats(request.getCapacity())
                .ticketBasePrice(request.getTicketBasePrice())
                .build();
        return flightRepository.save(flight);
    }

    private boolean isAvailableSeats(Flight flight, int requestedSeats) {
        return flight.getRemainingSeats() >= requestedSeats;
    }

    private void checkIsFlightAlreadySaved(FlightSaveRequest request) {
        boolean flightExists = flightRepository.existsByRouteIdAndAirlineId
                (request.getRouteId(), request.getAirlineId());
        if (flightExists) {
            throw new FlightAlreadySaveException("A flight with the same route, " +
                    "departure date, and airline already exists.");
        }
    }

    private void checkRouteAndAirlineExistence(FlightSaveRequest request) {
        routeService.checkIsRouteExist(request.getRouteId());
        airlineService.checkAirlineExist(request.getAirlineId());
    }

    private void validateFlightSaveRequest(FlightSaveRequest request) {
        boolean anyFieldBlank = Stream.of(request.getRouteId(),
                        request.getAirlineId(),
                        request.getCapacity(),
                        request.getTicketBasePrice())
                .anyMatch(Objects::isNull);
        if (anyFieldBlank) {
            throw new FlightValidationException("Required fields cannot be left blank");
        }
        if (request.getCapacity() < 15) {
            throw new FlightValidationException("Capacity is invalid");
        }
    }
}
