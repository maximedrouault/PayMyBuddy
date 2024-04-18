package com.paymybuddy.controller;

import com.paymybuddy.dto.CommissionDTO;
import com.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Controller for handling commissions related requests for future features.
 */
@RestController
@RequiredArgsConstructor
public class TransactionController {

    // Service for transaction-related operations
    private final TransactionService transactionService;

    /**
     * Handles GET requests to the /commissions endpoint.
     * Retrieves a list of all commissions.
     *
     * @return a list of CommissionDTO objects in JSON Format.
     */
    @GetMapping("/commissions")
    public List<CommissionDTO> getCommissions() {
        return transactionService.getCommissions();
    }
}