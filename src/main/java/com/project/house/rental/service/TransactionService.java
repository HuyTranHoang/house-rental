package com.project.house.rental.service;

import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TransactionService {

    Map<String, Object> getAllTransactionsWithParams(TransactionParams transactionParams);

    TransactionDto createTransaction(PaymentRequest paymentRequest, HttpServletRequest request) throws CustomRuntimeException;

    TransactionDto updateTransactionStatus(String txnRef, String status);

    TransactionDto findByTransactionId(String transactionId); //transactionId là Mã giao dịch khi thanh toán bằng VNPay
  
    Map<String, Object> getUserTransactions(HttpServletRequest request, TransactionParams transactionParams);
  
    void updateTransactionId(long id, String transactionId);

}

