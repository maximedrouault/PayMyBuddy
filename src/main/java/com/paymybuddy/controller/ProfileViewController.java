package com.paymybuddy.controller;

import com.paymybuddy.dto.WalletAddFormDTO;
import com.paymybuddy.dto.WalletWithdrawFormDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;

import static com.paymybuddy.constant.Constant.MAX_BALANCE;

@Controller
@RequiredArgsConstructor
public class ProfileViewController {

    private final UserService userService;


    @ModelAttribute
    public void populateModel(@NotNull Principal principal, Model model) {
        User currentUser = userService.getUserByEmail(principal.getName());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("walletAddFormDTO", new WalletAddFormDTO());
        model.addAttribute("walletWithdrawFormDTO", new WalletWithdrawFormDTO());
    }


    @GetMapping("/profile")
    public String profileView() {
        return "profile";
    }

    @PostMapping("/profile/add")
    public String addMoneyToWallet(@Valid @ModelAttribute WalletAddFormDTO walletAddFormDTO,
                                   @NotNull Principal principal, BindingResult result,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "profile";
        }

        User currentUser = userService.getUserByEmail(principal.getName());
        BigDecimal amountToAdd = walletAddFormDTO.getAmountToAdd();
        BigDecimal newBalance = currentUser.getWallet().getBalance().add(amountToAdd);

        if (newBalance.compareTo(MAX_BALANCE) > 0) {
            redirectAttributes.addFlashAttribute("errorAddMessage", "The balance cannot be greater than " + MAX_BALANCE + ". Please, enter a lower amount");

        } else {
            currentUser.getWallet().setBalance(newBalance);
            userService.updateUser(currentUser);
            redirectAttributes.addFlashAttribute("successAddMessage", "The balance successfully updated");
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/withdraw")
    public String withdrawMoneyFromWallet(@Valid @ModelAttribute WalletWithdrawFormDTO walletWithdrawFormDTO,
                                          @NotNull Principal principal, BindingResult result,
                                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "profile";
        }

        User currentUser = userService.getUserByEmail(principal.getName());
        BigDecimal amountToWithdraw = walletWithdrawFormDTO.getAmountToWithdraw();
        BigDecimal newBalance = currentUser.getWallet().getBalance().subtract(amountToWithdraw);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("errorWithdrawMessage", "The balance cannot be under 0. Please, enter an upper amount");

        } else {
            currentUser.getWallet().setBalance(newBalance);
            userService.updateUser(currentUser);
            redirectAttributes.addFlashAttribute("successWithdrawMessage", "The balance has been updated and the money sent to your bank account successfully");
        }

        return "redirect:/profile";
    }
}
