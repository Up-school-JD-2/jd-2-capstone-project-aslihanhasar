package io.upschool.ticketBooking.repository;

import io.upschool.ticketBooking.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    List<Airline> findByAirlineCodeContainingIgnoreCaseOrAirlineNameContainingIgnoreCase(String code, String name);

    @Query("SELECT COUNT(a) FROM Airline a WHERE a.airlineCode = :code OR a.airlineName = :name")
    int findCountByAirlineCodeContainingIgnoreCaseOrAirlineNameContainingIgnoreCase
            (@Param("code") String airlineCode, @Param("name") String airlineName);
}
