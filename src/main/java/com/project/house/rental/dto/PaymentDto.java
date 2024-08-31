package com.project.house.rental.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {

    String status;

    String message;

    String URL;

    String transactionId;

    //String txnRef;

}
