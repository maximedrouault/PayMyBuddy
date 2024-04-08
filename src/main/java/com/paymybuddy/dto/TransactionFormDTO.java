package com.paymybuddy.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

import static com.paymybuddy.constant.Constant.MAX_AMOUNT;
import static com.paymybuddy.constant.Constant.MIN_AMOUNT;

@Data
public class TransactionFormDTO {

    @NotNull(message = "Cannot be null. Please, make a choice")
    @Min(1)
    private Integer receiverUserId;

    @NotBlank(message = "Cannot be empty. Please, enter a description")
    @Length(max = 100, message = "The string must be 100 characters maximum")
    private String description;

    @DecimalMin(value = MIN_AMOUNT, message = "The amount must be superior or equal at " + MIN_AMOUNT)
    @DecimalMax(value = MAX_AMOUNT, message = "The amount cannot be greater than " + MAX_AMOUNT)
    @Digits(integer = 8, fraction = 2, message = "The amount must have 8 integer and 2 decimal max")
    @NotNull(message = "Cannot be null. Enter an amount")
    private BigDecimal transactionAmount;
}