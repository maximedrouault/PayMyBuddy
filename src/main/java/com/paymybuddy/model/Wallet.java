package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

import static com.paymybuddy.constant.Constant.MAX_AMOUNT;

/**
 * Entity class representing a wallet of a user in the PayMyBuddy application.
 * This class includes fields for the wallet's id and balance.
 * It also includes methods for adding and withdrawing money from the wallet.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int walletId;

    @Column(precision = 10, scale = 2, nullable = false)
    @Min(value = 0)
    @DecimalMax(value = MAX_AMOUNT)
    private BigDecimal balance;


    /**
     * Withdraws a specified amount from the wallet balance.
     *
     * @param amountToWithdraw The amount to be withdrawn from the wallet balance.
     * This method subtracts the specified amount from the current wallet balance.
     */
    public void withdrawMoney(BigDecimal amountToWithdraw) {
        this.balance = this.balance.subtract(amountToWithdraw);
    }

    /**
     * Adds a specified amount to the wallet balance.
     *
     * @param amountToAdd The amount to be added to the wallet balance.
     * This method adds the specified amount to the current wallet balance.
     */
    public void addMoney(BigDecimal amountToAdd) {
        this.balance = this.balance.add(amountToAdd);
    }
}