package com.paymybuddy.service;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.paymybuddy.constant.Constant.COMMISSION_FACTOR;

/**
 * Service class for managing transactions between users.
 * This class provides methods to get a list of transactions for a user and to add a new transaction between two users.
 * It uses the TransactionRepository to interact with the database.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    // TransactionRepository instance for accessing transaction data from the database
    private final TransactionRepository transactionRepository;


    /**
     * Retrieves a paginated list of transactions for a user.
     *
     * @param senderUserEmail the email of the user whose transactions are to be retrieved
     * @param currentPageNumber the current page number for pagination
     * @return a Page object containing Transaction objects representing the user's transactions
     */
    public Page<Transaction> getTransactions(String senderUserEmail, int currentPageNumber) {
        Pageable pageRequest = PageRequest.of(currentPageNumber, 3);

        return transactionRepository.findTransactionsBySender_EmailOrderByDateDescTimeDesc(senderUserEmail, pageRequest);
    }

    /**
     * Adds a new transaction between two users.
     *
     * @param senderUser the user who is sending the transaction
     * @param receiverUser the user who is receiving the transaction
     * @param description the description of the transaction
     * @param transactionAmount the amount of the transaction
     * @param commissionAmount the commission amount for the transaction
     * @return the Transaction object representing the new transaction
     */
    public Transaction addTransaction(User senderUser, User receiverUser, String description, BigDecimal transactionAmount, BigDecimal commissionAmount) {

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

    /**
     * Retrieves a list of commissions for all transactions.
     *
     * @return a list of CommissionDTO objects representing the commissions for all transactions
     */
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

    /**
     * Calculates the commission amount for a transaction.
     *
     * @param transactionAmount the amount of the transaction
     * @return the commission amount
     */
    public BigDecimal getCommissionAmount(BigDecimal transactionAmount) {
        BigDecimal commissionAmount = transactionAmount.multiply(COMMISSION_FACTOR);

        return roundAmount(commissionAmount);
    }

    /**
     * Rounds a BigDecimal amount to two decimal places.
     *
     * @param amount the amount to round
     * @return the rounded amount
     */
    public BigDecimal roundAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}