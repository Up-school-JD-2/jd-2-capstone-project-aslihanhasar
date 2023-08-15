package io.upschool.ticketBooking.entity;

import io.upschool.ticketBooking.enums.TicketClass;
import io.upschool.ticketBooking.enums.TicketStatus;
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
@Table(name = "tickets")
@AttributeOverride(
        name = "id",
        column = @Column(
                name = "ticket_id"
        )
)
public class Ticket extends BaseEntity {
    @Column(unique = true)
    private String ticketNumber;
    @Column(nullable = false)
    private String passengerName;
    @Column(nullable = false)
    private String creditCardNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    @Column(nullable = false)
    private int passengerCount;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Enumerated(EnumType.STRING)
    private TicketClass ticketClass;
    private Boolean cancelled;
    private BigDecimal ticketPrice;
}
