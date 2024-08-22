package com.project.house.rental.dto.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionParams extends PaginationParams {

    private long userId;

    private String status;

    private double minAmount;

    private double maxAmount;

    private String sortBy = "transactionDateDesc";

}
