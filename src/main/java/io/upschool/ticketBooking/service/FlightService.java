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

/**
 * The FlightService class is a service that handles operations related to flights,
 * such as saving, retrieving, and managing flight data. It interacts with
 * the FlightRepository for data storage and relies on the RouteService and
 * AirlineService for route and airline-related operations.
 */
@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final RouteService routeService;
    private final AirlineService airlineService;

    /**
     * Saves a flight based on the provided FlightSaveRequest.
     *
     * @param request The FlightSaveRequest containing the details of the flight to be saved.
     * @return A FlightSaveResponse indicating the result of the save operation.
     * @throws FlightValidationException  If any of the provided request fields are null or invalid.
     * @throws FlightAlreadySaveException If a flight with the same route, departure date, and airline already exists.
     */
    public FlightSaveResponse save(FlightSaveRequest request) {
        validateFlightSaveRequest(request);
        checkRouteAndAirlineExistence(request);
        Flight savedFlight = buildFlightAndSave(request);
        return convertFlightToResponse(savedFlight);
    }

    /**
     * Retrieves a list of flights based on the provided departure and arrival keys and departure date.
     *
     * @param departureKey  The search key for departure airport's location.
     * @param arrivalKey    The search key for arrival airport's location.
     * @param departureDate The departure date to filter flights by.
     * @return A list of FlightSearchResponse objects representing the retrieved flights.
     * @throws FlightNotFoundException If no flights are found matching the search criteria.
     */
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

    /**
     * Reserves the specified number of seats on the given flight if enough seats are available.
     *
     * @param flight         The Flight for which to reserve seats.
     * @param requestedSeats The number of seats to reserve.
     * @throws NotAvailableSeatException If there are not enough available seats for the reservation.
     */
    protected void reserveSeats(Flight flight, int requestedSeats) {
        boolean availableSeats = isAvailableSeats(flight, requestedSeats);
        if (!availableSeats) {
            throw new NotAvailableSeatException("Not enough available seats.");
        }
        int remainingSeats = flight.getRemainingSeats();
        flight.setRemainingSeats(remainingSeats - requestedSeats);
        flightRepository.save(flight);
    }

    /**
     * Updates the available seats count on the given flight by adding the specified number of seats.
     *
     * @param flight         The Flight for which to update available seats.
     * @param requestedSeats The number of seats to add to the available seats count.
     */
    protected void updateAvailableSeats(Flight flight, int requestedSeats) {
        int currentAvailableSeats = flight.getRemainingSeats();
        int updatedAvailableSeats = currentAvailableSeats + requestedSeats;
        flight.setRemainingSeats(updatedAvailableSeats);
        flightRepository.save(flight);
    }

    /**
     * Retrieves a Flight based on the provided ID.
     *
     * @param id The ID of the Flight to retrieve.
     * @return The retrieved Flight.
     * @throws FlightNotFoundException If no Flight is found with the provided ID.
     */
    protected Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found."));
    }

    /**
     * Converts a Flight entity to a FlightSaveResponse object.
     *
     * @param flight The Flight entity to be converted.
     * @return A FlightSaveResponse object representing the converted entity.
     */
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
