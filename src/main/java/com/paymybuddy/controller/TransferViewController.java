package com.paymybuddy.controller;


import com.paymybuddy.dto.ConnectionForm;
import com.paymybuddy.dto.TransactionForm;
import com.paymybuddy.model.Connection;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferViewController {

    private final ConnectionService connectionService;
    private final TransactionService transactionService;
    private final UserService userService;


    @ModelAttribute
    public void populateModel(@RequestParam @NotNull int currentUserId, @RequestParam(defaultValue = "0") @Min(0) int currentPageNumber, Model model) {
        List<Connection> connections = connectionService.getConnections(currentUserId);
        Page<Transaction> transactions = transactionService.getTransactions(currentUserId, currentPageNumber);
        List<User> connectableUsers = userService.getConnectableUsers(currentUserId);

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connectableUsers", connectableUsers);
        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("connectionForm", new ConnectionForm());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("currentPageNumber", currentPageNumber);
    }


    @GetMapping("/transfer")
    public String transferView() {
        return "transfer";
    }

    @PostMapping("/connection")
    public String addConnection(@RequestParam @NotNull int currentUserId,
                                @Valid @ModelAttribute ConnectionForm connectionForm,
                                BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "transfer";
        }

        User ownerUser = userService.getUserById(currentUserId);
        User receiverUser = userService.getUserById(connectionForm.getReceiverUserId());

        connectionService.addConnection(ownerUser, receiverUser);
        redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully");

        return "redirect:/transfer?currentUserId=" + currentUserId;
    }

    @PostMapping("/transaction")
    public String addTransaction(@RequestParam @NotNull int currentUserId,
                                 @Valid @ModelAttribute TransactionForm transactionForm,
                                 BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "transfer";
        }

        User senderUser = userService.getUserById(currentUserId);
        User receiverUser = userService.getUserById(transactionForm.getReceiverUserId());

        if (senderUser.getWallet().getBalance() < transactionForm.getTransactionAmount()) {
            redirectAttributes.addFlashAttribute("errorMessage", "The balance cannot be under 0. Please, add money to your wallet before transaction");

            return "redirect:/transfer?currentUserId=" + currentUserId;

        } else {
            Transaction savedTransaction = transactionService.saveTransaction(senderUser, receiverUser, transactionForm.getDescription(), transactionForm.getTransactionAmount());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction added successfully  : Sender - " + savedTransaction.getSender().getName() +
                            " / Receiver - " + savedTransaction.getReceiver().getName() +
                            " / Transaction Amount : " + savedTransaction.getTransactionAmount() + " €" +
                            " / Commission amount : " + savedTransaction.getCommissionAmount() + " €");
        }

        return "redirect:/transfer?currentUserId=" + currentUserId;
    }
}