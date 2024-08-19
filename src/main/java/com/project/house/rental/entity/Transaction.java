package com.project.house.rental.entity;

import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String transactionId;

    @Enumerated(EnumType.STRING)
    TransactionType type;

    long amount;

    @Column(name = "transaction_date")
    Date transactionDate;

    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL
    }

    public enum TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED
    }
}
