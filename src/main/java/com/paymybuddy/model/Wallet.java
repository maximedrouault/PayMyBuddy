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


    public void withdrawMoney(BigDecimal amountToWithdraw) {
        this.balance = this.balance.subtract(amountToWithdraw);
    }

    public void addMoney(BigDecimal amountToAdd) {
        this.balance = this.balance.add(amountToAdd);
    }
}