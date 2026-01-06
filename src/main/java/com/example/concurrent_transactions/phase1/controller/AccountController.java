package com.example.concurrent_transactions.phase1.controller;

import com.example.concurrent_transactions.phase1.dto.AccountRequest;
import com.example.concurrent_transactions.phase1.dto.AccountResponse;
import com.example.concurrent_transactions.phase1.service.AccountService;
import com.example.concurrent_transactions.util.AccountCreator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/phase1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final AccountCreator accountCreator;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("POST /api/phase1/accounts - Creating account: {}", request.accountNumber());
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        log.debug("GET /api/phase1/accounts/{}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        log.debug("GET /api/phase1/accounts/number/{}", accountNumber);
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        log.debug("GET /api/phase1/accounts");
        List<AccountResponse> responses = accountService.getAllAccounts();
        return ResponseEntity.ok(responses);
    }

    /**
     * Creates multiple test accounts for testing purposes.
     * POST /api/phase1/accounts/create-test?count=5&balance=1000.00
     */
    @PostMapping("/create-test")
    public ResponseEntity<List<AccountResponse>> createTestAccounts(
            @RequestParam(defaultValue = "3") int count,
            @RequestParam(defaultValue = "1000.00") BigDecimal balance) {
        log.info("POST /api/phase1/accounts/create-test - Creating {} test accounts with balance {}", count, balance);
        List<AccountResponse> responses = accountCreator.createTestAccounts(count, balance);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}

