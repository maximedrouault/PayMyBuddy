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

/**
 * Entity class representing a transaction between two users.
 * This class is used to represent a transaction between two users in the database.
 * It contains the ID of the transaction, the date and time of the transaction, the description of the transaction,
 * the amount of the transaction, the commission amount for the transaction, and the IDs of the sender and receiver users.
 */
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


    /**
     * The sender of the transaction.
     * This field is a reference to the User entity that is the sender of the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    @NotNull
    private User sender;

    /**
     * The receiver of the transaction.
     * This field is a reference to the User entity that is the receiver of the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    @NotNull
    private User receiver;
}