package com.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionFormDTO {

    @NotNull(message = "Cannot be null. Please, make a choice")
    private Integer receiverUserId;

    @NotBlank(message = "Cannot be empty. Please, enter a description")
    private String description;

    @DecimalMin(value = "0.01", message = "The amount must be superior or equal at 0.01")
    @Digits(integer = 10, fraction = 2, message = "The amount must have 10 integer and 2 decimal max")
    @NotNull(message = "Cannot be null. Enter an amount")
    private Double transactionAmount;
}