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

/**
 * The AirlineService class is a service that handles operations related to airlines,
 * such as saving, retrieving, and converting airline data.
 * It uses the AirlineRepository to interact with the underlying data storage.
 */
@Service
@RequiredArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;

    /**
     * Saves an airline based on the provided AirlineSaveRequest.
     *
     * @param request The AirlineSaveRequest containing the details of the airline to be saved.
     * @return An AirlineSaveResponse indicating the result of the save operation.
     * @throws AirlineValidationException  If the provided request contains empty or blank fields.
     * @throws AirlineAlreadySaveException If an airline with the same name or code already exists.
     */
    @Transactional
    public AirlineSaveResponse save(AirlineSaveRequest request) {
        validateAirlineSaveRequest(request);
        checkIsAirlineAlreadySaved(request);
        Airline savedAirline = buildAndSaveAirline(request);
        return convertAirlineToResponse(savedAirline);
    }

    /**
     * Retrieves a list of airlines based on the provided search key.
     *
     * @param searchKey The search key to filter airlines by name or code.
     * @return A list of AirlineSaveResponse objects representing the retrieved airlines.
     */
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

    /**
     * Converts a list of Airline entities to a list of AirlineSaveResponse objects.
     *
     * @param airlines The list of Airline entities to be converted.
     * @return A list of AirlineSaveResponse objects.
     */
    public List<AirlineSaveResponse> convertAirlinesToResponses(List<Airline> airlines) {
        return airlines.stream()
                .map(this::convertAirlineToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a set of airlines based on the provided set of airline IDs.
     *
     * @param airlineIds The set of airline IDs to retrieve airlines for.
     * @return A set of Airline entities.
     * @throws AirlineNotFoundException If any of the provided airline IDs do not correspond to existing airlines.
     */
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

    /**
     * Retrieves an airline based on the provided airline ID.
     *
     * @param id The ID of the airline to retrieve.
     * @return An Airline entity corresponding to the provided ID.
     * @throws AirlineNotFoundException If no airline is found with the provided ID.
     */
    protected Airline getAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found."));
    }

    /**
     * Checks if an airline exists based on the provided airline ID.
     *
     * @param airlineId The ID of the airline to check for existence.
     * @throws AirlineNotFoundException If no airline is found with the provided ID.
     */
    protected void checkAirlineExist(Long airlineId) {
        boolean existAirline = airlineRepository.existsById(airlineId);
        if (!existAirline) {
            throw new AirlineNotFoundException("Airline not found with id: " + airlineId);
        }
    }

    /**
     * Converts an Airline entity to an AirlineSaveResponse object.
     *
     * @param airline The Airline entity to be converted.
     * @return An AirlineSaveResponse object representing the converted entity.
     */
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
