package com.example.concurrent_transactions.phase1.dto;

import jakarta.validation.constraints.NotBlank;

public record SeatRequest(
        @NotBlank(message = "Seat number is required")
        String seatNumber
) {
}

