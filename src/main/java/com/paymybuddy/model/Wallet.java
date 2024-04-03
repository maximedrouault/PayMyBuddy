package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Entity
@Data
@DynamicUpdate
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int walletId;

    @Column(precision = 10, scale = 2, nullable = false)
    @Min(value = 0)
    @DecimalMax(value = "99999999.99")
    private BigDecimal balance;


    public void withdrawMoney(BigDecimal amountToWithdraw) {
        this.balance = this.balance.subtract(amountToWithdraw);
    }

    public void addMoney(BigDecimal amountToAdd) {
        this.balance = this.balance.add(amountToAdd);
    }
}