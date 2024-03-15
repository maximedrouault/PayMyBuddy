package com.paymybuddy.service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;


    public List<Transaction> getTransactions(int senderUserId) {
        return transactionRepository.findTransactionsBySender_UserId(senderUserId);
    }

    public Transaction saveTransaction(User senderUser, User receiverUser, String description, double amount) {
        double commissionFactor = 0.05; // 0.05 is the commission factor (5%)
        double commission = Math.round((amount * commissionFactor) * 100.0) / 100.0; //  the result is rounded
        amount = Math.round(amount * 100.0) / 100.0;

        Transaction transactionToSave = Transaction.builder()
                .date(LocalDate.now())
                .time(LocalTime.now().truncatedTo(ChronoUnit.SECONDS))
                .description(description)
                .sender(senderUser)
                .receiver(receiverUser)
                .amount(amount)
                .commission(commission)
                .build();

        walletService.withdrawMoney(senderUser.getWallet(), amount + commission);
        walletService.addMoney(receiverUser.getWallet(), amount);

        return transactionRepository.save(transactionToSave);
    }
}