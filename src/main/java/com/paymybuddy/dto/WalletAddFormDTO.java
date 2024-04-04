package com.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletAddFormDTO {

    @DecimalMin(value = "0.01", message = "The amount must be superior or equal at 0.01")
    @DecimalMax(value = "99999999.99", message = "The amount cannot be greater than 99999999,99")
    @Digits(integer = 8, fraction = 2, message = "The amount must have 8 integer and 2 decimal max")
    @NotNull(message = "Cannot be null. Enter an amount")
    private BigDecimal amountToAdd;
}