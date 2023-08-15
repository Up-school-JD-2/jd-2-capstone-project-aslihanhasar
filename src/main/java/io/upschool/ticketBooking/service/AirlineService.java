package io.upschool.ticketBooking.service;

import io.micrometer.common.util.StringUtils;
import io.upschool.ticketBooking.dto.request.AirlineSaveRequest;
import io.upschool.ticketBooking.dto.response.AirlineSaveResponse;
import io.upschool.ticketBooking.entity.Airline;
import io.upschool.ticketBooking.exception.AirlineAlreadySaveException;
import io.upschool.ticketBooking.exception.AirlineNotFoundException;
import io.upschool.ticketBooking.exception.AirlineValidationException;
import io.upschool.ticketBooking.repository.AirlineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;

    @Transactional
    public AirlineSaveResponse save(AirlineSaveRequest request) {
        validateAirlineSaveRequest(request);
        checkIsAirlineAlreadySaved(request);
        Airline savedAirline = buildAndSaveAirline(request);
        return convertAirlineToResponse(savedAirline);
    }

    public List<AirlineSaveResponse> getAllAirlines(String searchKey) {
        List<Airline> airlines;
        if (searchKey.isEmpty()) {
            airlines = airlineRepository.findAll();
        } else {
            airlines = airlineRepository.findByAirlineCodeContainingIgnoreCaseOrAirlineNameContainingIgnoreCase
                    (searchKey, searchKey);
        }
        return airlines.stream()
                .map(this::convertAirlineToResponse)
                .toList();
    }

    public List<AirlineSaveResponse> convertAirlinesToResponses(List<Airline> airlines) {
        return airlines.stream()
                .map(this::convertAirlineToResponse)
                .collect(Collectors.toList());
    }

    protected Set<Airline> getAirlinesByIds(Set<Long> airlineIds) {
        Set<Airline> airlines = airlineRepository.findAllById(airlineIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (airlines.size() < airlineIds.size()) {
            throw new AirlineNotFoundException("Invalid id. Airline or airlines not found.");
        }
        return airlines;
    }

    protected Airline getAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(()-> new AirlineNotFoundException("Airline not found."));
    }

    protected void checkAirlineExist(Long airlineId) {
        boolean existAirline = airlineRepository.existsById(airlineId);
        if (!existAirline) {
            throw new AirlineNotFoundException("Airline not found with id: " + airlineId);
        }
    }

    protected AirlineSaveResponse convertAirlineToResponse(Airline airline) {
        return AirlineSaveResponse
                .builder()
                .airlineId(airline.getId())
                .airline(airline.getAirlineName() + " - " + airline.getAirlineCode())
                .build();
    }

    private Airline buildAndSaveAirline(AirlineSaveRequest request) {
        Airline airline = Airline
                .builder()
                .airlineName(request.getAirlineName())
                .airlineCode(request.getAirlineCode())
                .build();
        return airlineRepository.save(airline);
    }

    private void validateAirlineSaveRequest(AirlineSaveRequest request) {
        boolean anyFieldBlank = Stream.of(request.getAirlineCode(),
                        request.getAirlineName())
                .anyMatch(StringUtils::isBlank);
        if (anyFieldBlank) {
            throw new AirlineValidationException("Airline code or airline name cannot be left blank");
        }
    }

    private void checkIsAirlineAlreadySaved(AirlineSaveRequest request) {
        int count = airlineRepository
                .findCountByAirlineCodeContainingIgnoreCaseOrAirlineNameContainingIgnoreCase(
                        request.getAirlineCode().trim(), request.getAirlineName().trim());
        if (count > 0) {
            throw new AirlineAlreadySaveException("An airline with the same name or code already exist");
        }
    }
}
