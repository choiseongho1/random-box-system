package com.randombox.api.v1.ticket.dto;

import com.randombox.domain.ticket.Ticket;
import com.randombox.domain.ticket.TicketReservation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class TicketResponse {

    @Getter
    public static class TicketInfo {
        private final Long id;
        private final String name;
        private final String description;
        private final Integer price;
        private final Integer quantity;
        private final LocalDateTime eventDateTime;
        private final String venue;
        private final LocalDateTime salesStartTime;
        private final LocalDateTime salesEndTime;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
        private final boolean onSale;

        @Builder
        public TicketInfo(Ticket ticket) {
            this.id = ticket.getId();
            this.name = ticket.getName();
            this.description = ticket.getDescription();
            this.price = ticket.getPrice();
            this.quantity = ticket.getQuantity();
            this.eventDateTime = ticket.getEventDateTime();
            this.venue = ticket.getVenue();
            this.salesStartTime = ticket.getSalesStartTime();
            this.salesEndTime = ticket.getSalesEndTime();
            this.createdAt = ticket.getCreatedAt();
            this.updatedAt = ticket.getUpdatedAt();
            this.onSale = ticket.isOnSale();
        }
    }

    @Getter
    public static class ReservationInfo {
        private final Long id;
        private final Long userId;
        private final String userEmail;
        private final Long ticketId;
        private final String ticketName;
        private final LocalDateTime reservationDateTime;
        private final String status;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        @Builder
        public ReservationInfo(TicketReservation reservation) {
            this.id = reservation.getId();
            this.userId = reservation.getUser().getId();
            this.userEmail = reservation.getUser().getEmail();
            this.ticketId = reservation.getTicket().getId();
            this.ticketName = reservation.getTicket().getName();
            this.reservationDateTime = reservation.getReservationDateTime();
            this.status = reservation.getStatus().name();
            this.createdAt = reservation.getCreatedAt();
            this.updatedAt = reservation.getUpdatedAt();
        }
    }
}
