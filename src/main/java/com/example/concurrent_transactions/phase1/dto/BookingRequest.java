package com.example.concurrent_transactions.phase1.dto;

import com.example.concurrent_transactions.phase1.entity.Booking.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "Seat ID is required")
        Long seatId,

        @NotNull(message = "Booking status is required")
        BookingStatus bookingStatus,

        @NotBlank(message = "Customer name is required")
        String customerName
) {
}

