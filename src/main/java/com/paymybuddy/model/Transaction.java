package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.paymybuddy.constant.Constant.MAX_AMOUNT;
import static com.paymybuddy.constant.Constant.MIN_AMOUNT;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Check(constraints = "sender_user_id != receiver_user_id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "time")
    @NotNull
    private LocalTime time;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    @NotNull
    @DecimalMin(value = MIN_AMOUNT)
    @DecimalMax(value = MAX_AMOUNT)
    private BigDecimal transactionAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    @Min(value = 0)
    @DecimalMax(value = MAX_AMOUNT)
    private BigDecimal commissionAmount;


    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    @NotNull
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    @NotNull
    private User receiver;
}