package com.paymybuddy.unit.service;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.model.Wallet;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    // getTransactions
    @Test
    void getTransactions_WhenTransactionsExist_ShouldReturnTransactions() {
        String senderUserEmail = "senderuser@example.com";
        int currentPageNumber = 0;
        Transaction transaction1 = Transaction.builder().build();
        Transaction transaction2 = Transaction.builder().build();
        List<Transaction> expectedTransactions = List.of(transaction1, transaction2);
        Pageable pageable = PageRequest.of(currentPageNumber, 3);
        Page<Transaction> page = new PageImpl<>(expectedTransactions);

        when(transactionRepository.findTransactionsBySender_EmailOrderByDateDescTimeDesc(any(), eq(pageable))).thenReturn(page);

        Page<Transaction> retrievedTransactions = transactionService.getTransactions(senderUserEmail, currentPageNumber);

        assertEquals(2, retrievedTransactions.getContent().size());
        assertEquals(expectedTransactions, retrievedTransactions.getContent());
    }

    @Test
    public void getTransactions_WhenNoTransactionsExist_ShouldReturnEmptyPage() {
        String senderUserEmail = "senderuser@example.com";
        int currentPageNumber = 0;
        Pageable pageable = PageRequest.of(0, 3);

        when(transactionRepository.findTransactionsBySender_EmailOrderByDateDescTimeDesc(any(), eq(pageable))).thenReturn(Page.empty());

        Page<Transaction> retrievedTransactions = transactionService.getTransactions(senderUserEmail, currentPageNumber);

        assertEquals(0, retrievedTransactions.getContent().size());
    }

    @Test
    void getTransactions_WhenFourTransactionsExistOnTwoPages_ShouldReturnOnlyOneTransactionOnPageTwo() {
        String senderUserEmail = "senderuser@example.com";
        int currentPageNumber = 1;
        Transaction transaction1 = Transaction.builder().transactionId(1).build();
        Transaction transaction2 = Transaction.builder().transactionId(2).build();
        Transaction transaction3 = Transaction.builder().transactionId(3).build();
        Transaction transaction4 = Transaction.builder().transactionId(4).build();
        List<Transaction> expectedTransactions = List.of(transaction1, transaction2, transaction3, transaction4);
        Pageable pageable = PageRequest.of(currentPageNumber, 3);
        Page<Transaction> page = new PageImpl<>(expectedTransactions.subList(3, 4));

        when(transactionRepository.findTransactionsBySender_EmailOrderByDateDescTimeDesc(anyString(), eq(pageable))).thenReturn(page);

        Page<Transaction> retrievedTransactions = transactionService.getTransactions(senderUserEmail, currentPageNumber);

        assertEquals(1, retrievedTransactions.getContent().size());
        assertEquals(transaction4, retrievedTransactions.getContent().get(0));
    }


    // addTransaction
    @Test
    void addTransaction_WhenTransactionToAddExists_ShouldReturnAddedTransaction() {
        User senderUser = User.builder().wallet(Wallet.builder().balance(BigDecimal.ZERO).build()).build();
        User receiverUser = User.builder().wallet(Wallet.builder().balance(BigDecimal.ZERO).build()).build();
        String description = "Test description";
        BigDecimal transactionAmount = BigDecimal.ZERO;
        BigDecimal commissionAmount = BigDecimal.ZERO;
        Transaction transactionToAdd = Transaction.builder().build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionToAdd);

        Transaction addedTransaction = transactionService.addTransaction(senderUser, receiverUser, description, transactionAmount, commissionAmount);

        assertEquals(addedTransaction, transactionToAdd);
    }

    @Test
    void addTransaction_WhenValidTransactionToAddExists_ShouldReturnAddedTransaction() {
        User senderUser = User.builder().wallet(Wallet.builder().balance(BigDecimal.valueOf(100.00)).build()).build();
        User receiverUser = User.builder().wallet(Wallet.builder().balance(BigDecimal.valueOf(100.00)).build()).build();
        String description = "Test description";
        BigDecimal transactionAmount = BigDecimal.valueOf(50.00);
        BigDecimal commissionAmount = BigDecimal.valueOf(2.50);
        Transaction transactionToAdd = Transaction.builder()
                .date(LocalDate.of(2024, 4, 10))
                .time(LocalTime.of(13,45))
                .description(description)
                .sender(senderUser)
                .receiver(receiverUser)
                .transactionAmount(transactionAmount)
                .commissionAmount(commissionAmount)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionToAdd);

        Transaction addedTransaction = transactionService.addTransaction(senderUser, receiverUser, description, transactionAmount, commissionAmount);

        assertEquals(addedTransaction, transactionToAdd);
        assertEquals(BigDecimal.valueOf(47.50), senderUser.getWallet().getBalance()); // The sender pays the transactionAmount + commissionAmount
        assertEquals(BigDecimal.valueOf(150.00), receiverUser.getWallet().getBalance()); // The receiver receive the transactionAmount
    }


    // getCommissions
    @Test
    void getCommissions_WhenTransactionsExist_ShouldReturnCommissions() {
        Transaction transaction1 = Transaction.builder()
                .transactionId(1)
                .date(LocalDate.of(2024, 4, 10))
                .time(LocalTime.of(13,45))
                .commissionAmount(BigDecimal.valueOf(2.50))
                .build();
        Transaction transaction2 = Transaction.builder()
                .transactionId(2)
                .date(LocalDate.of(2024, 4, 11))
                .time(LocalTime.of(14,30))
                .commissionAmount(BigDecimal.valueOf(3.00))
                .build();

        when(transactionRepository.findAll()).thenReturn(List.of(transaction1, transaction2));

        List<CommissionDTO> retrievedCommissions = transactionService.getCommissions();

        assertEquals(2, retrievedCommissions.size());
        // CommissionDTO 1
        assertEquals(1, retrievedCommissions.get(0).getTransactionId());
        assertEquals(LocalDate.of(2024, 4, 10), retrievedCommissions.get(0).getDate());
        assertEquals(LocalTime.of(13,45), retrievedCommissions.get(0).getTime());
        assertEquals(BigDecimal.valueOf(2.50), retrievedCommissions.get(0).getCommissionAmount());
        // CommissionDTO 2
        assertEquals(2, retrievedCommissions.get(1).getTransactionId());
        assertEquals(LocalDate.of(2024, 4, 11), retrievedCommissions.get(1).getDate());
        assertEquals(LocalTime.of(14,30), retrievedCommissions.get(1).getTime());
        assertEquals(BigDecimal.valueOf(3.00), retrievedCommissions.get(1).getCommissionAmount());
    }

    @Test
    void getCommissions_WhenNoTransactionsExist_ShouldReturnEmptyList() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<CommissionDTO> retrievedCommissions = transactionService.getCommissions();

        assertEquals(0, retrievedCommissions.size());
    }


    // getCommissionAmount
    @Test
    void getCommissionAmount_WhenTransactionAmountIsProvided_ShouldReturnCommissionAmount() {
        BigDecimal transactionAmount = BigDecimal.valueOf(100.00);
        BigDecimal expectedCommission = transactionAmount.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP); // 0.05 is the COMMISSION_FACTOR (5%)

        BigDecimal actualCommission = transactionService.getCommissionAmount(transactionAmount);

        assertEquals(expectedCommission, actualCommission);
    }

    @Test
    void getCommissionAmount_WhenTransactionAmountIsZero_ShouldReturnZero() {
        BigDecimal transactionAmount = BigDecimal.ZERO;
        BigDecimal expectedCommission = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        BigDecimal actualCommission = transactionService.getCommissionAmount(transactionAmount);

        assertEquals(expectedCommission, actualCommission);
    }


    // roundAmount
    @Test
    void roundAmount_WhenAmountHasMoreThanTwoDecimalPlaces_ShouldRoundToTwoDecimalPlaces() {
        BigDecimal amount = BigDecimal.valueOf(100.1234);

        BigDecimal roundedAmount = transactionService.roundAmount(amount);

        assertEquals(BigDecimal.valueOf(100.12), roundedAmount);
    }

    @Test
    void roundAmount_WhenAmountHasLessThanTwoDecimalPlaces_ShouldReturnSameAmount() {
        BigDecimal amount = BigDecimal.valueOf(100.1);

        BigDecimal roundedAmount = transactionService.roundAmount(amount);

        assertEquals(BigDecimal.valueOf(100.10).setScale(2,RoundingMode.HALF_UP), roundedAmount);
    }

    @Test
    void roundAmount_WhenAmountHasExactlyTwoDecimalPlaces_ShouldReturnSameAmount() {
        BigDecimal amount = BigDecimal.valueOf(100.12);

        BigDecimal roundedAmount = transactionService.roundAmount(amount);

        assertEquals(BigDecimal.valueOf(100.12), roundedAmount);
    }

    @Test
    void roundAmount_WhenAmountIsZero_ShouldReturnZero() {
        BigDecimal amount = BigDecimal.ZERO;

        BigDecimal roundedAmount = transactionService.roundAmount(amount);

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), roundedAmount);
    }
}