package com.project.house.rental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDto {

    long id;

    String transactionId;

    @NotEmpty(message = "Transaction type is required")
    String transactionType;

    long userId;

    String username;

    @Min(value = 50000, message = "Amount must be greater than 50,000")
    @Max(value = 1000000, message = "Amount must be less than 1,000,000")
    long amount;

    Date transactionDate;

    String status;

    String description;

}