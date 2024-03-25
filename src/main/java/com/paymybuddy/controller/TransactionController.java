package com.paymybuddy.controller;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam int senderUserId) {
        return transactionService.getTransactions(senderUserId);
    }

//    @PostMapping("/transaction")
//    public Transaction saveTransaction(@RequestParam int senderUserId, int receiverUserId, String description, double transactionAmount) {
//        User senderUser = userService.getUserById(senderUserId);
//        User receiverUser = userService.getUserById(receiverUserId);
//
//        return transactionService.saveTransaction(senderUser, receiverUser, description, transactionAmount);
//    }

    @GetMapping("/commissions")
    public List<CommissionDTO> getCommissions() {
        return transactionService.getCommissions();
    }
}