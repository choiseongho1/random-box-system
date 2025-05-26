package com.randombox.domain.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    @Query("SELECT t FROM Ticket t WHERE t.salesStartTime <= :now AND t.salesEndTime >= :now")
    List<Ticket> findAllOnSale(LocalDateTime now);
    
    List<Ticket> findByEventDateTimeAfter(LocalDateTime now);
}
