package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Entity class representing a connection between two users.
 * This class is used to represent a connection between two users in the database.
 * It contains the ID of the connection, the ID of the owner user, and the ID of the receiver user.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"owner_user_id", "receiver_user_id"}))
@Check(constraints = "owner_user_id != receiver_user_id")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int connectionId;


    /**
     * The owner of the connection.
     * This field is a reference to the User entity that owns the connection.
     */
    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    @NotNull
    private User owner;

    /**
     * The receiver of the connection.
     * This field is a reference to the User entity that is the receiver of the connection.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    @NotNull
    private User receiver;
}