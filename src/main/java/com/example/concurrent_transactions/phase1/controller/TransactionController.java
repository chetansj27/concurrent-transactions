package com.example.concurrent_transactions.phase1.controller;

import com.example.concurrent_transactions.phase1.dto.TransactionRequest;
import com.example.concurrent_transactions.phase1.dto.TransactionResponse;
import com.example.concurrent_transactions.phase1.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phase1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        log.info("POST /api/phase1/transactions/deposit - Account: {}, Amount: {}", 
                request.accountId(), request.amount());
        TransactionResponse response = transactionService.deposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        log.info("POST /api/phase1/transactions/withdraw - Account: {}, Amount: {}", 
                request.accountId(), request.amount());
        TransactionResponse response = transactionService.withdraw(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable Long accountId) {
        log.debug("GET /api/phase1/transactions/account/{}", accountId);
        List<TransactionResponse> responses = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        log.debug("GET /api/phase1/transactions/{}", id);
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }
}

