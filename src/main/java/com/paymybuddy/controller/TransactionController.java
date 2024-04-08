package com.paymybuddy.controller;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @GetMapping("/commissions")
    public List<CommissionDTO> getCommissions() {
        return transactionService.getCommissions();
    }
}