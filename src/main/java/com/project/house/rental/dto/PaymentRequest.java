package com.project.house.rental.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {

    @Min(value = 50000, message = "Amount must be greater than 50,000")
    @Max(value = 1000000, message = "Amount must be less than 1,000,000")
    long amount;

    @NotEmpty(message = "Transaction type is required")
    String type;

    String description;
}
