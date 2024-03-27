package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

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
    @NotNull
    private int connectionId;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    @NotNull
    private User owner;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    @NotNull
    private User receiver;
}