package io.upschool.ticketBooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "airports")
@AttributeOverride(
        name = "id",
        column = @Column(
                name = "airport_id"
        )
)
public class Airport extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String airportName;
    @Column(nullable = false, unique = true)
    private String airportCode;
    @Column(nullable = false)
    private String airportLocation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "airport_airline",
            joinColumns = @JoinColumn(name = "airport_id"),
            inverseJoinColumns = @JoinColumn(name = "airline_id")
    )
    private Set<Airline> airlines;
}
