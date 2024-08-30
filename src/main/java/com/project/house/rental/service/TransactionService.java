package com.project.house.rental.service;

import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

public interface TransactionService {

    Map<String, Object> getAllTransactionsWithParams(TransactionParams transactionParams);

    TransactionDto createTransaction(PaymentRequest paymentRequest, HttpServletRequest request);

    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);

    void updateTransactionStatus(String txnRef, String status);

    TransactionDto findByTransactionId(String transactionId); //Id khi thanh toán bằng VNPay

    TransactionDto updateTransactionId(long id, String transactionId);
  
}

