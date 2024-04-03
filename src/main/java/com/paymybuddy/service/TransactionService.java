package com.paymybuddy.service;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public Page<Transaction> getTransactions(String senderUserEmail, int currentPageNumber) {
        Sort orderedTransactions = Sort.by(Sort.Order.desc("date"), Sort.Order.desc("time"));
        Pageable paginatedTransactions = PageRequest.of(currentPageNumber, 3, orderedTransactions);

        return transactionRepository.findTransactionsBySender_Email(senderUserEmail, paginatedTransactions);
    }

    public Transaction saveTransaction(User senderUser, User receiverUser, String description, BigDecimal transactionAmount, BigDecimal commissionAmount) {

        Transaction transactionToSave = Transaction.builder()
                .date(LocalDate.now())
                .time(LocalTime.now().truncatedTo(ChronoUnit.SECONDS))
                .description(description)
                .sender(senderUser)
                .receiver(receiverUser)
                .transactionAmount(transactionAmount)
                .commissionAmount(commissionAmount)
                .build();

        senderUser.getWallet().withdrawMoney(transactionAmount.add(commissionAmount));
        receiverUser.getWallet().addMoney(transactionAmount);

        return transactionRepository.save(transactionToSave);
    }

    public List<CommissionDTO> getCommissions() {
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();

        return transactions.stream()
                .map(transaction -> CommissionDTO.builder()
                        .transactionId(transaction.getTransactionId())
                        .date(transaction.getDate())
                        .time(transaction.getTime())
                        .commissionAmount(transaction.getCommissionAmount())
                        .build())
                .toList();
    }

    public BigDecimal getCommissionAmount(BigDecimal transactionAmount) {
        BigDecimal commissionFactor = BigDecimal.valueOf(0.05); // 0.05 is the commission factor (5%)
        BigDecimal commissionAmount = transactionAmount.multiply(commissionFactor);

        return roundAmount(commissionAmount);
    }

    public BigDecimal roundAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}