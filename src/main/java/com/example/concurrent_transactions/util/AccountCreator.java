package com.example.concurrent_transactions.util;

import com.example.concurrent_transactions.phase1.dto.AccountRequest;
import com.example.concurrent_transactions.phase1.dto.AccountResponse;
import com.example.concurrent_transactions.phase1.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class to create test accounts.
 * 
 * Usage:
 * Inject AccountCreator and call:
 * - createAccount(accountNumber, holderName, balance) - Create single account
 * - createTestAccounts(count, balance) - Create multiple test accounts
 * - createRandomAccount(holderName, balance) - Create account with random number
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccountCreator {

    private final AccountService accountService;

    /**
     * Creates a single test account with specified parameters.
     */
    public AccountResponse createAccount(String accountNumber, String accountHolderName, BigDecimal initialBalance) {
        log.info("Creating account: {} for {} with initial balance {}", accountNumber, accountHolderName, initialBalance);
        
        AccountRequest request = new AccountRequest(
                accountNumber,
                accountHolderName,
                initialBalance
        );

        try {
            AccountResponse response = accountService.createAccount(request);
            log.info("Account created successfully - ID: {}, Number: {}, Balance: {}", 
                    response.id(), response.accountNumber(), response.balance());
            return response;
        } catch (Exception e) {
            log.error("Failed to create account: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Creates multiple test accounts with default naming.
     */
    public List<AccountResponse> createTestAccounts(int count, BigDecimal initialBalance) {
        log.info("Creating {} test accounts with initial balance {}", count, initialBalance);
        
        List<AccountResponse> createdAccounts = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            String accountNumber = "TEST-ACC-" + i;
            String accountHolderName = "Test User " + i;
            
            try {
                AccountResponse account = createAccount(accountNumber, accountHolderName, initialBalance);
                createdAccounts.add(account);
                log.info("[{}/{}] Created account: {} (ID: {})", i, count, accountNumber, account.id());
            } catch (Exception e) {
                log.error("[{}/{}] Failed to create account: {} - {}", i, count, accountNumber, e.getMessage());
            }
        }
        
        log.info("Successfully created {}/{} accounts", createdAccounts.size(), count);
        return createdAccounts;
    }

    /**
     * Creates a test account with random account number.
     */
    public AccountResponse createRandomAccount(String accountHolderName, BigDecimal initialBalance) {
        Random random = new Random();
        String accountNumber = "ACC-" + (1000 + random.nextInt(9000));
        return createAccount(accountNumber, accountHolderName, initialBalance);
    }
}

