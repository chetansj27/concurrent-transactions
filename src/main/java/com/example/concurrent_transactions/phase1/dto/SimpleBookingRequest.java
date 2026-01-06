package com.example.concurrent_transactions.phase1.dto;

public record SimpleBookingRequest(
        Long seatId,
        String customerName
) {
}

