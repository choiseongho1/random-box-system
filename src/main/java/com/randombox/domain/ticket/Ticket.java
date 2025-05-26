package com.randombox.domain.ticket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime eventDateTime;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDateTime salesStartTime;

    @Column(nullable = false)
    private LocalDateTime salesEndTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Ticket(String name, String description, Integer price, Integer quantity,
                 LocalDateTime eventDateTime, String venue,
                 LocalDateTime salesStartTime, LocalDateTime salesEndTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.eventDateTime = eventDateTime;
        this.venue = venue;
        this.salesStartTime = salesStartTime;
        this.salesEndTime = salesEndTime;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String name, String description, Integer price, Integer quantity,
                      LocalDateTime eventDateTime, String venue,
                      LocalDateTime salesStartTime, LocalDateTime salesEndTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.eventDateTime = eventDateTime;
        this.venue = venue;
        this.salesStartTime = salesStartTime;
        this.salesEndTime = salesEndTime;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new IllegalStateException("티켓이 모두 소진되었습니다.");
        }
        this.quantity--;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOnSale() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(salesStartTime) && now.isBefore(salesEndTime);
    }
}
