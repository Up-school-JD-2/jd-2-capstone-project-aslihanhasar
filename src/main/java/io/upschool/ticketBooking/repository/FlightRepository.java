package io.upschool.ticketBooking.repository;

import io.upschool.ticketBooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByRoute_DepartureAirport_AirportLocationContainingIgnoreCaseAndRoute_ArrivalAirport_AirportLocationContainingIgnoreCaseAndRoute_DepartureDate(
            String departureLocation, String arrivalLocation, LocalDate departureDate);

    boolean existsByRouteIdAndAirlineId(Long routeId, Long airlineId);
}
