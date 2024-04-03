package com.paymybuddy.controller;


import com.paymybuddy.dto.ConnectionFormDTO;
import com.paymybuddy.dto.TransactionFormDTO;
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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferViewController {

    private final ConnectionService connectionService;
    private final TransactionService transactionService;
    private final UserService userService;


    @ModelAttribute
    public void populateModel(@NotNull Principal principal, @RequestParam(defaultValue = "0") @Min(0) int currentPageNumber, Model model) {
        List<Connection> connections = connectionService.getConnections(principal.getName());
        Page<Transaction> transactions = transactionService.getTransactions(principal.getName(), currentPageNumber);
        List<User> connectableUsers = userService.getConnectableUsers(principal.getName());

        model.addAttribute("connections", connections);
        model.addAttribute("transactions", transactions);
        model.addAttribute("connectableUsers", connectableUsers);
        model.addAttribute("transactionFormDTO", new TransactionFormDTO());
        model.addAttribute("connectionFormDTO", new ConnectionFormDTO());
        model.addAttribute("pages", new int[transactions.getTotalPages()]);
        model.addAttribute("currentPageNumber", currentPageNumber);
    }


    @GetMapping("/transfer")
    public String transferView() {
        return "transfer";
    }

    @PostMapping("/connection")
    public String addConnection(@NotNull Principal principal,
                                @Valid @ModelAttribute ConnectionFormDTO connectionFormDTO,
                                BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "transfer";
        }

        User ownerUser = userService.getUserByEmail(principal.getName());
        User receiverUser = userService.getUserById(connectionFormDTO.getReceiverUserId());

        Connection addedConnection = connectionService.addConnection(ownerUser, receiverUser);

        if (addedConnection != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Connection added successfully");

            return "redirect:/transfer";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Connection unsuccessfully added");
        }

        return "redirect:/transfer";
    }

    @PostMapping("/transaction")
    public String addTransaction(@NotNull Principal principal,
                                 @Valid @ModelAttribute TransactionFormDTO transactionFormDTO,
                                 BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "transfer";
        }

        User senderUser = userService.getUserByEmail(principal.getName());
        User receiverUser = userService.getUserById(transactionFormDTO.getReceiverUserId());
        BigDecimal transactionAmount = transactionService.roundAmount(transactionFormDTO.getTransactionAmount());
        BigDecimal commissionAmount = transactionService.getCommissionAmount(transactionAmount);

        if (senderUser.getWallet().getBalance().compareTo(transactionAmount.add(commissionAmount)) < 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "The balance cannot be under 0. Please, add money to your wallet before transaction");

            return "redirect:/transfer";

        } else {
            Transaction savedTransaction = transactionService.saveTransaction(senderUser, receiverUser, transactionFormDTO.getDescription(), transactionAmount, commissionAmount);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Transaction completed successfully  : Sender - " + savedTransaction.getSender().getName() +
                            " / Receiver - " + savedTransaction.getReceiver().getName() +
                            " / Transaction Amount : " + savedTransaction.getTransactionAmount() + " €" +
                            " / Commission amount : " + savedTransaction.getCommissionAmount() + " €");
        }

        return "redirect:/transfer";
    }
}