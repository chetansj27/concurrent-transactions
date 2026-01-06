package com.example.concurrent_transactions.phase1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AccountRequest(
        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotBlank(message = "Account holder name is required")
        String accountHolderName,

        @NotNull(message = "Initial balance is required")
        @PositiveOrZero(message = "Initial balance must be positive or zero")
        BigDecimal initialBalance
) {
}
