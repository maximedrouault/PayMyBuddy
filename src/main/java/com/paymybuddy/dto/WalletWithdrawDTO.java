package com.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import static com.paymybuddy.constant.Constant.MAX_AMOUNT;
import static com.paymybuddy.constant.Constant.MIN_AMOUNT;

@Data
public class WalletWithdrawDTO {

    @DecimalMin(value = MIN_AMOUNT, message = "The amount must be superior or equal at " + MIN_AMOUNT)
    @DecimalMax(value = MAX_AMOUNT, message = "The amount cannot be greater than " + MAX_AMOUNT)
    @Digits(integer = 8, fraction = 2, message = "The amount must have 8 integer and 2 decimal max")
    @NotNull(message = "Cannot be null. Enter an amount")
    private BigDecimal amountToWithdraw;
}