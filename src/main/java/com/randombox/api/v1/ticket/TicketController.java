package com.randombox.api.v1.ticket;

import com.randombox.api.v1.ticket.dto.TicketRequest;
import com.randombox.api.v1.ticket.dto.TicketResponse;
import com.randombox.domain.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse.TicketInfo> createTicket(
            @Valid @RequestBody TicketRequest.Create request) {
        return ResponseEntity.ok(ticketService.createTicket(request));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse.TicketInfo> getTicket(
            @PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicket(ticketId));
    }

    @GetMapping("/on-sale")
    public ResponseEntity<List<TicketResponse.TicketInfo>> getTicketsOnSale() {
        return ResponseEntity.ok(ticketService.getTicketsOnSale());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<TicketResponse.TicketInfo>> getUpcomingTickets() {
        return ResponseEntity.ok(ticketService.getUpcomingTickets());
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse.TicketInfo> updateTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketRequest.Update request) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, request));
    }

    @PostMapping("/{ticketId}/reserve")
    public ResponseEntity<TicketResponse.ReservationInfo> reserveTicket(
            @PathVariable Long ticketId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(ticketService.reserveTicket(userId, ticketId));
    }

    @PostMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<TicketResponse.ReservationInfo> cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(ticketService.cancelReservation(userId, reservationId));
    }

    @GetMapping("/users/{userId}/reservations")
    public ResponseEntity<List<TicketResponse.ReservationInfo>> getUserReservations(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getUserReservations(userId));
    }
}
