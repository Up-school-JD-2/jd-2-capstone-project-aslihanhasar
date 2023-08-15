package io.upschool.ticketBooking.repository;

import io.upschool.ticketBooking.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findByAirportCodeContainingIgnoreCaseOrAirportNameContainingIgnoreCase
            (String code, String name);

    boolean existsByAirportNameIgnoreCase(String airportName);

    boolean existsByAirportCodeIgnoreCase(String airportCode);

}
