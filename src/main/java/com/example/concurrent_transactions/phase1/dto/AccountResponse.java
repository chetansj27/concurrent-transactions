package com.example.concurrent_transactions.phase1.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountNumber,
        BigDecimal balance,
        String accountHolderName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
