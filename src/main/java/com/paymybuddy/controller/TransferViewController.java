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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferViewController {

    private final ConnectionService connectionService;
    private final TransactionService transactionService;
    private final UserService userService;


    @GetMapping("/transfer")
    public String transferView(@RequestParam int currentUserId, Model model) {
        List<Connection> connections = connectionService.getConnections(currentUserId);
        List<Transaction> transactions = transactionService.getTransactions(currentUserId);
        List<User> connectableUsers = userService.getConnectableUsers(currentUserId);

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connectableUsers", connectableUsers);

        return "transfer";
    }

    @PostMapping("/connection")
    public String addConnection(@RequestParam int ownerUserId, @RequestParam int receiverUserId) {
        User ownerUser = userService.getUserById(ownerUserId);
        User receiverUser = userService.getUserById(receiverUserId);

        connectionService.addConnection(ownerUser, receiverUser);

        return "redirect:/transfer?currentUserId=" + ownerUserId;
    }

    @PostMapping("/transaction")
    public String addTransaction(@RequestParam int senderUserId, @RequestParam int receiverUserId, @RequestParam double transactionAmount) {
        User senderUser = userService.getUserById(senderUserId);
        User receiverUser = userService.getUserById(receiverUserId);
        String description = "Test description"; // TODO : Not present in UI Design

        transactionService.saveTransaction(senderUser, receiverUser, description, transactionAmount);

        return "redirect:/transfer?currentUserId=" + senderUserId;
    }
}
