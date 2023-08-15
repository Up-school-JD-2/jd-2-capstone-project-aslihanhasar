package io.upschool.ticketBooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "airlines")
@AttributeOverride(
        name = "id",
        column = @Column(
                name = "airline_id"
        )
)
public class Airline extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String airlineName;
    @Column(nullable = false, unique = true)
    private String airlineCode;
}


