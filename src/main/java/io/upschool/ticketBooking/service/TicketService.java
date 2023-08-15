package io.upschool.ticketBooking.service;

import io.upschool.ticketBooking.dto.request.TicketPurchaseRequest;
import io.upschool.ticketBooking.dto.response.FlightSaveResponse;
import io.upschool.ticketBooking.dto.response.TicketPurchaseResponse;
import io.upschool.ticketBooking.entity.Flight;
import io.upschool.ticketBooking.entity.Ticket;
import io.upschool.ticketBooking.enums.TicketClass;
import io.upschool.ticketBooking.enums.TicketStatus;
import io.upschool.ticketBooking.exception.*;
import io.upschool.ticketBooking.repository.TicketRepository;
import io.upschool.ticketBooking.utils.CreditCardUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final FlightService flightService;

    @Transactional
    public TicketPurchaseResponse purchaseTicket(TicketPurchaseRequest request) {
        validateTicketPurchaseRequest(request);
        Ticket savedTicket=buildTicketAndSave(request);
        return convertTicketToResponse(savedTicket);
    }

    public void checkInTicket(String ticketNumber) {
        Ticket ticket=getByTicketNumber(ticketNumber);
        if (ticket.getStatus() == TicketStatus.PURCHASED) {
            ticket.setStatus(TicketStatus.CHECKED_IN);
            ticketRepository.save(ticket);
        } else {
            throw new AlreadyCheckedInException("Ticket already checked in");
        }
    }

    public void cancelTicket(String ticketNumber) {
        Ticket ticket=getByTicketNumber(ticketNumber);
        if (ticket.getStatus()==TicketStatus.CHECKED_IN){
            throw new TicketOperationException("Cannot cancel a checked ticket.");
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        ticket.setCancelled(true);
        flightService.updateAvailableSeats(ticket.getFlight(),ticket.getPassengerCount());
        ticketRepository.save(ticket);
    }

    public Ticket getByTicketNumber(String ticketNumber){
        return ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found."));

    }

    public TicketPurchaseResponse convertTicketToResponse(Ticket ticket) {
        FlightSaveResponse flightSaveResponse=flightService.
                convertFlightToResponse(ticket.getFlight());
        return TicketPurchaseResponse
                .builder()
                .ticketNumber(ticket.getTicketNumber())
                .passengerName(ticket.getPassengerName())
                .flightSaveResponse(flightSaveResponse)
                .ticketClass(ticket.getTicketClass())
                .maskedCreditCardNumber(ticket.getCreditCardNumber())
                .totalPrice(ticket.getTicketPrice())
                .build();
    }

    private Ticket buildTicketAndSave(TicketPurchaseRequest request) {
        Flight flight = flightService.getFlightById(request.getFlightId());
        flightService.reserveSeats(flight, request.getPassengerCount());
        TicketClass ticketClass=TicketClass.fromValue(request.getTicketClass());
        BigDecimal ticketPrice=calculateTicketPrice(request,flight);
        String maskedCreditCarNumber= CreditCardUtils.maskCreditCardNumber(request.getCreditCardNumber());
        makePayment(maskedCreditCarNumber);
        String ticketNumber=generateUniqueTicketNumber();
        Ticket ticket = Ticket
                .builder()
                .ticketNumber(ticketNumber)
                .passengerName(request.getPassengerName())
                .creditCardNumber(maskedCreditCarNumber)
                .flight(flight)
                .passengerCount(request.getPassengerCount())
                .status(TicketStatus.PURCHASED)
                .ticketClass(ticketClass)
                .ticketPrice(ticketPrice)
                .cancelled(false)
                .build();
        return ticketRepository.save(ticket);
    }

    private BigDecimal calculateTicketPrice(TicketPurchaseRequest request,Flight flight) {
        TicketClass ticketClass = TicketClass.fromValue(request.getTicketClass());
        BigDecimal classMultiplier = getTicketClassMultiplier(ticketClass);
        BigDecimal basePrice = flight.getTicketBasePrice();
        return basePrice.multiply(classMultiplier).multiply
                (BigDecimal.valueOf(request.getPassengerCount()));
    }

    private BigDecimal getTicketClassMultiplier(TicketClass ticketClass) {
        return switch (ticketClass) {
            case BUSINESS_CLASS -> BigDecimal.valueOf(1.5);
            case FIRST_CLASS -> BigDecimal.valueOf(2.0);
            default -> throw new InvalidTicketClassException
                    ("Unsupported ticket class");
        };
    }

    private boolean isPaymentSuccess(){
        return true;
    }

    private String generateUniqueTicketNumber() {
        int CODE_LENGTH = 8;
        SecureRandom secureRandom = new SecureRandom();
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomStringBuilder = new StringBuilder(CODE_LENGTH);
        while (randomStringBuilder.length() < CODE_LENGTH) {
            int randomCharIndex = secureRandom.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomCharIndex);
            randomStringBuilder.append(randomChar);
        }
        return randomStringBuilder.toString();
    }
    private void makePayment(String maskedCreditCardNumber){
        if(!isPaymentSuccess()){
            throw new PaymentFailedException("Payment process failed.");
        }
    }

    private void validateTicketPurchaseRequest(TicketPurchaseRequest request) {
        boolean anyFieldBlank = Stream.of(request.getPassengerName(),
                        request.getFlightId(),
                        request.getPassengerCount(),
                        request.getTicketClass(),
                        request.getCreditCardNumber())
                .anyMatch(Objects::isNull);
        if (anyFieldBlank) {
            throw new TicketValidationException("Required fields cannot be left blank");
        }
    }
      /*
    private String generateUniqueTicketNumber() {
        int CODE_LENGTH = 8;
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[CODE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(randomBytes);
    }
     */
}



