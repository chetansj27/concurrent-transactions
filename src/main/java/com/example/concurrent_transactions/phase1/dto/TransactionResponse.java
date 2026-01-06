package com.example.concurrent_transactions.phase1.dto;

import com.example.concurrent_transactions.phase1.entity.Transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long accountId,
        TransactionType transactionType,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        String description,
        LocalDateTime createdAt
) {
}
