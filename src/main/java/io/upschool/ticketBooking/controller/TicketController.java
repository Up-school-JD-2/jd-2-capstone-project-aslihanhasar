package io.upschool.ticketBooking.controller;

import io.upschool.ticketBooking.dto.BaseResponse;
import io.upschool.ticketBooking.dto.request.TicketPurchaseRequest;
import io.upschool.ticketBooking.dto.response.TicketPurchaseResponse;
import io.upschool.ticketBooking.entity.Ticket;
import io.upschool.ticketBooking.service.BaseResponseService;
import io.upschool.ticketBooking.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final BaseResponseService baseResponseService;
    @GetMapping("/{ticketNumber}")
    public ResponseEntity<BaseResponse<TicketPurchaseResponse>> getTicketByNumber(@PathVariable String ticketNumber) {
        Ticket ticket = ticketService.getByTicketNumber(ticketNumber);
        TicketPurchaseResponse response=ticketService.convertTicketToResponse(ticket);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, response);
    }
    @PostMapping("/purchase")
    public ResponseEntity<BaseResponse<TicketPurchaseResponse>> purchaseTicket
            (@Valid @RequestBody TicketPurchaseRequest request) {
        TicketPurchaseResponse ticketResponse = ticketService.purchaseTicket(request);
        return baseResponseService.createSuccessResponse(HttpStatus.OK, ticketResponse);
    }

    @PostMapping("/check-in/{ticketNumber}")
    public ResponseEntity<BaseResponse<String>> checkInTicket(@PathVariable String ticketNumber) {
         ticketService.checkInTicket(ticketNumber);
        return baseResponseService.createSuccessResponse(HttpStatus.OK,"Checked in.");
    }

    @PostMapping("/cancel/{ticketNumber}")
    public ResponseEntity<BaseResponse<String>> cancelTicket(@PathVariable String ticketNumber) {
        ticketService.cancelTicket(ticketNumber);
        return baseResponseService.createSuccessResponse(HttpStatus.OK,"Ticket Cancelled.");
    }

}
