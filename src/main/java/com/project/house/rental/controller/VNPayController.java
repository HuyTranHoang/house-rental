package com.project.house.rental.controller;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.entity.Transaction;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.service.TransactionService;
import com.project.house.rental.service.auth.UserService;
import com.project.house.rental.service.vnPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VNPayService vnPayService;
    private final TransactionService transactionService;
    private final UserService userService;

    @Value("${base.client-url}")
    private String baseUrl;

    private static final String VNPAY_SUCCESS_CODE = "00";
    private static final String SUCCESS_URL = "thanh-toan-thanh-cong";
    private static final String FAILURE_URL = "thanh-toan-that-bai";

    public VNPayController(VNPayService vnPayService, TransactionService transactionService, UserService userService) {
        this.vnPayService = vnPayService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        PaymentDto payment = vnPayService.createPayment(paymentRequest, request);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/return")
    public ResponseEntity<Void> returnPayment(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef
    ) throws CustomRuntimeException {
        String redirectUrl = VNPAY_SUCCESS_CODE.equals(responseCode)
                ? handleSuccess(txnRef)
                : handleFailure(txnRef);

        return ResponseEntity.status(302)
                .header("Location", baseUrl + redirectUrl)
                .build();
    }

    private String handleSuccess(String txnRef) throws CustomRuntimeException {
        TransactionDto transactionDto = transactionService.updateTransactionStatus(txnRef, Transaction.TransactionStatus.SUCCESS.toString());
        userService.updateBalance(transactionDto.getUserId(), transactionDto.getAmount());
        return SUCCESS_URL + "?vnp_TxnRef=" + txnRef;
    }

    private String handleFailure(String txnRef) {
        transactionService.updateTransactionStatus(txnRef, Transaction.TransactionStatus.FAILED.toString());
        return FAILURE_URL + "?vnp_TxnRef=" + txnRef;
    }
}
