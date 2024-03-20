package com.paymybuddy.service;

import com.paymybuddy.model.Wallet;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    public void withdrawMoney(Wallet wallet, double amountToWithdraw) {
        wallet.setBalance(wallet.getBalance() - amountToWithdraw);
    }

    public void addMoney(Wallet wallet, double amountToAdd) {
        wallet.setBalance(wallet.getBalance() + amountToAdd);
    }
}