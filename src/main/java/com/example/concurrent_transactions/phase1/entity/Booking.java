package com.example.concurrent_transactions.phase1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "booking_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "status_before", nullable = false)
    private Boolean statusBefore;

    @Column(name = "status_after", nullable = false)
    private Boolean statusAfter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum BookingStatus {
        BOOKED,
        CANCELLED
    }
}

