package com.randombox.domain.ticket;

import com.randombox.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketReservationRepository extends JpaRepository<TicketReservation, Long> {
    List<TicketReservation> findByUserOrderByReservationDateTimeDesc(User user);
    List<TicketReservation> findByTicketOrderByReservationDateTimeDesc(Ticket ticket);
}
