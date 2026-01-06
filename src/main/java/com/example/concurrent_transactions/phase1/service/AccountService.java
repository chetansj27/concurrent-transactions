package com.example.concurrent_transactions.phase1.service;

import com.example.concurrent_transactions.phase1.dto.AccountRequest;
import com.example.concurrent_transactions.phase1.dto.AccountResponse;
import com.example.concurrent_transactions.phase1.entity.Account;
import com.example.concurrent_transactions.phase1.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse createAccount(AccountRequest request) {
        log.info("Creating account with number: {}", request.accountNumber());
        
        if (accountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            throw new IllegalArgumentException("Account with number " + request.accountNumber() + " already exists");
        }

        Account account = Account.builder()
                .accountNumber(request.accountNumber())
                .accountHolderName(request.accountHolderName())
                .balance(request.initialBalance())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created with ID: {}", savedAccount.getId());
        
        return mapToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        log.debug("Fetching account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + id));
        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        log.debug("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with number: " + accountNumber));
        return mapToResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        log.debug("Fetching all accounts");
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountHolderName(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}

