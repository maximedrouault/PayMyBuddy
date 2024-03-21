package com.paymybuddy.controller;


import com.paymybuddy.model.Connection;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VueController {

    private final ConnectionService connectionService;
    private final TransactionService transactionService;
    private final UserService userService;


    @GetMapping("/transfer")
    public String transfer(@RequestParam int currentUserId, Model model) {
        List<Connection> connections = connectionService.getConnections(currentUserId);
        List<Transaction> transactions = transactionService.getTransactions(currentUserId);
        List<User> connectableUsers = userService.getConnectableUsers(currentUserId);

        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connectableUsers", connectableUsers);

        return "transfer";
    }
}
