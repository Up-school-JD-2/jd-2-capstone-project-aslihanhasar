package io.upschool.ticketBooking.service;

import io.micrometer.common.util.StringUtils;
import io.upschool.ticketBooking.dto.request.AddAirlineToAirportRequest;
import io.upschool.ticketBooking.dto.request.AirportSaveRequest;
import io.upschool.ticketBooking.dto.response.AddAirlineToAirportResponse;
import io.upschool.ticketBooking.dto.response.AirportDetailResponse;
import io.upschool.ticketBooking.dto.response.AirportSaveResponse;
import io.upschool.ticketBooking.entity.Airline;
import io.upschool.ticketBooking.entity.Airport;
import io.upschool.ticketBooking.exception.AirportAlreadySaveException;
import io.upschool.ticketBooking.exception.AirportNotFoundException;
import io.upschool.ticketBooking.exception.AirportValidationException;
import io.upschool.ticketBooking.repository.AirportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;
    private final AirlineService airlineService;

    @Transactional
    public AirportSaveResponse save(AirportSaveRequest request) {
        validateAirportSaveRequest(request);
        checkIsAirportAlreadySaved(request);
        Airport savedAirport = buildAndSaveAirport(request);
        return convertAirportToResponse(savedAirport);
    }

    public List<AirportSaveResponse> getAllAirports(String searchKey) {
        List<Airport> airports = getAirportsBySearchKey(searchKey);
        if (airports.isEmpty()) {
            throw new AirportNotFoundException("No airports found matching the search criteria.");
        }
        return airports.stream()
                .map(this::convertAirportToResponse)
                .toList();
    }

    public AirportDetailResponse getAirportDetails(Long airportId) {
        Airport airport = getAirportById(airportId);
        List<Airline> airlines = new ArrayList<>(airport.getAirlines());
        return AirportDetailResponse.builder()
                .airport(convertAirportToResponse(airport))
                .airlines(airlineService.convertAirlinesToResponses(airlines))
                .build();
    }

    @Transactional
    public AddAirlineToAirportResponse addAirlineToAirport(AddAirlineToAirportRequest request) {
        Airport airport = getAirportById(request.getAirportId());
        Set<Airline> airlines = airlineService.getAirlinesByIds(request.getAirlineIds());
        airport.getAirlines().addAll(airlines);
        Airport savedAirport = airportRepository.save(airport);
        List<Long> savedAirlineIds = savedAirport.getAirlines().stream()
                .map(Airline::getId)
                .collect(Collectors.toList());
        return AddAirlineToAirportResponse.builder()
                .airportId(request.getAirportId())
                .airlineIds(savedAirlineIds)
                .build();
    }

    public void checkIsAirportExist(Long airportId) {
        boolean existAirport = airportRepository.existsById(airportId);
        if (!existAirport) {
            throw new AirportNotFoundException("Airport not found with id");
        }
    }

    protected Airport getAirportById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException("Airport Not Found."));
    }

    protected AirportSaveResponse convertAirportToResponse(Airport airport) {
        return AirportSaveResponse.builder()
                .airportId(airport.getId())
                .airport(airport.getAirportName() + " - " + airport.getAirportCode())
                .airportLocation(airport.getAirportLocation())
                .build();
    }

    private List<Airport> getAirportsBySearchKey(String searchKey) {
        if (StringUtils.isBlank(searchKey)) {
            return airportRepository.findAll();
        } else {
            return airportRepository.findByAirportCodeContainingIgnoreCaseOrAirportNameContainingIgnoreCase(
                    searchKey, searchKey
            );
        }
    }

    private Airport buildAndSaveAirport(AirportSaveRequest request) {
        Airport airport = Airport.builder()
                .airportName(request.getAirportName())
                .airportCode(request.getAirportCode())
                .airportLocation(request.getAirportLocation().toUpperCase())
                .build();
        return airportRepository.save(airport);
    }

    private void validateAirportSaveRequest(AirportSaveRequest request) {
        if (StringUtils.isBlank(request.getAirportName())
                || StringUtils.isBlank(request.getAirportCode())
                || StringUtils.isBlank(request.getAirportLocation())) {
            throw new AirportValidationException("Airport name, airport code and " +
                    "location cannot be left blank");
        }
    }

    private void checkIsAirportAlreadySaved(AirportSaveRequest request) {
        String normalizedAirportName = request.getAirportName().trim().toLowerCase();
        String normalizedAirportCode = request.getAirportCode().trim().toUpperCase();
        boolean exists = airportRepository.existsByAirportNameIgnoreCase(normalizedAirportName)
                || airportRepository.existsByAirportCodeIgnoreCase(normalizedAirportCode);
        if (exists) {
            throw new AirportAlreadySaveException("An airport with the same name or code already exists");
        }
    }
}
