package io.upschool.ticketBooking.repository;

import io.upschool.ticketBooking.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByDepartureAirport_AirportLocationIgnoreCaseOrArrivalAirport_AirportLocationIgnoreCase(
            String departureLocation, String arrivalLocation);

    boolean existsByDepartureDateAndDepartureTimeAndDepartureAirportIdAndArrivalAirportId(
            LocalDate departureDate, LocalTime departureTime, Long departureAirportId, Long arrivalAirportId);

}
