package io.upschool.ticketBooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "flights")
@AttributeOverride(
        name = "id",
        column = @Column(
                name = "flight_id"
        )
)
public class Flight extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_route_id", nullable = false)
    private Route route;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;
    @Column(nullable = false)
    private int capacity;
    private BigDecimal ticketBasePrice;
    private int remainingSeats;
}
