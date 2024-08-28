package com.project.house.rental.service;

import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.ReviewParams;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    Map<String, Object> getAllTransactionsWithParams(TransactionParams transactionParams);

    TransactionDto createTransaction(TransactionDto transactionDto, HttpServletRequest request);

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);

}