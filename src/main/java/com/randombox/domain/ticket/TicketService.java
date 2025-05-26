package com.randombox.domain.ticket;

import com.randombox.api.v1.ticket.dto.TicketRequest;
import com.randombox.api.v1.ticket.dto.TicketResponse;
import com.randombox.domain.user.User;
import com.randombox.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketResponse.TicketInfo createTicket(TicketRequest.Create request) {
        validateTicketTimes(request.getEventDateTime(), request.getSalesStartTime(), request.getSalesEndTime());

        Ticket ticket = Ticket.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .eventDateTime(request.getEventDateTime())
                .venue(request.getVenue())
                .salesStartTime(request.getSalesStartTime())
                .salesEndTime(request.getSalesEndTime())
                .build();

        return new TicketResponse.TicketInfo(ticketRepository.save(ticket));
    }

    private void validateTicketTimes(LocalDateTime eventDateTime, LocalDateTime salesStartTime, LocalDateTime salesEndTime) {
        if (salesEndTime.isBefore(salesStartTime)) {
            throw new IllegalArgumentException("판매 종료 시간은 판매 시작 시간 이후여야 합니다.");
        }
        if (eventDateTime.isBefore(salesEndTime)) {
            throw new IllegalArgumentException("이벤트 시간은 판매 종료 시간 이후여야 합니다.");
        }
    }

    public TicketResponse.TicketInfo getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));
        return new TicketResponse.TicketInfo(ticket);
    }

    public List<TicketResponse.TicketInfo> getTicketsOnSale() {
        return ticketRepository.findAllOnSale(LocalDateTime.now()).stream()
                .map(TicketResponse.TicketInfo::new)
                .toList();
    }

    public List<TicketResponse.TicketInfo> getUpcomingTickets() {
        return ticketRepository.findByEventDateTimeAfter(LocalDateTime.now()).stream()
                .map(TicketResponse.TicketInfo::new)
                .toList();
    }

    @Transactional
    public TicketResponse.TicketInfo updateTicket(Long ticketId, TicketRequest.Update request) {
        validateTicketTimes(request.getEventDateTime(), request.getSalesStartTime(), request.getSalesEndTime());

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        ticket.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity(),
                request.getEventDateTime(),
                request.getVenue(),
                request.getSalesStartTime(),
                request.getSalesEndTime()
        );

        return new TicketResponse.TicketInfo(ticket);
    }

    @Transactional
    public TicketResponse.ReservationInfo reserveTicket(Long userId, Long ticketId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        if (!ticket.isOnSale()) {
            throw new IllegalStateException("현재 판매 중인 티켓이 아닙니다.");
        }

        ticket.decreaseQuantity();

        TicketReservation reservation = TicketReservation.builder()
                .user(user)
                .ticket(ticket)
                .build();

        return new TicketResponse.ReservationInfo(reservationRepository.save(reservation));
    }

    @Transactional
    public TicketResponse.ReservationInfo cancelReservation(Long userId, Long reservationId) {
        TicketReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예매입니다."));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 예매를 취소할 권한이 없습니다.");
        }

        reservation.cancel();
        return new TicketResponse.ReservationInfo(reservation);
    }

    public List<TicketResponse.ReservationInfo> getUserReservations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return reservationRepository.findByUserOrderByReservationDateTimeDesc(user).stream()
                .map(TicketResponse.ReservationInfo::new)
                .toList();
    }
}
