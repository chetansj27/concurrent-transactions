package com.example.concurrent_transactions.phase1.dto;

import com.example.concurrent_transactions.phase1.entity.Booking.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long seatId,
        BookingStatus bookingStatus,
        String customerName,
        Boolean statusBefore,
        Boolean statusAfter,
        LocalDateTime createdAt
) {
}

