package com.example.concurrent_transactions.phase1.dto;

import java.time.LocalDateTime;

public record SeatResponse(
        Long id,
        String seatNumber,
        Boolean isAvailable,
        String customerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

