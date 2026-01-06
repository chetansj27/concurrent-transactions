package com.example.concurrent_transactions.phase1.service;

import com.example.concurrent_transactions.phase1.dto.TransactionRequest;
import com.example.concurrent_transactions.phase1.dto.TransactionResponse;
import com.example.concurrent_transactions.phase1.entity.Account;
import com.example.concurrent_transactions.phase1.entity.Transaction;
import com.example.concurrent_transactions.phase1.repository.AccountRepository;
import com.example.concurrent_transactions.phase1.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionResponse deposit(TransactionRequest request) {
        log.info("Processing deposit: accountId={}, amount={}", request.accountId(), request.amount());
        return processTransaction(request.accountId(), request.amount(), 
                Transaction.TransactionType.DEPOSIT, request.description());
    }

    public TransactionResponse withdraw(TransactionRequest request) {
        log.info("Processing withdrawal: accountId={}, amount={}", request.accountId(), request.amount());
        return processTransaction(request.accountId(), request.amount(), 
                Transaction.TransactionType.WITHDRAWAL, request.description());
    }

    private TransactionResponse processTransaction(Long accountId, BigDecimal amount, 
                                                   Transaction.TransactionType type, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter;

        if (type == Transaction.TransactionType.WITHDRAWAL) {
            if (balanceBefore.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance. Current balance: " + balanceBefore);
            }
            balanceAfter = balanceBefore.subtract(amount);
        } else {
            balanceAfter = balanceBefore.add(amount);
        }

        account.setBalance(balanceAfter);
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType(type)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(description)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction completed: transactionId={}, balanceBefore={}, balanceAfter={}", 
                savedTransaction.getId(), balanceBefore, balanceAfter);

        return mapToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        log.debug("Fetching transactions for account ID: {}", accountId);
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        log.debug("Fetching transaction with ID: {}", id);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));
        return mapToResponse(transaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceBefore(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}

