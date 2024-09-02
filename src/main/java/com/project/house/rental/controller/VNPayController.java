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
    public ResponseEntity<String> returnPayment(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef
    ) throws CustomRuntimeException {

        String redirectUrl;

        if ("00".equals(responseCode)) {
            redirectUrl = baseUrl + handleSuccess(txnRef);
        } else {
            redirectUrl = baseUrl + handleFailure(txnRef);
        }

        return ResponseEntity.status(302).header("Location", redirectUrl).build();
    }

    private String handleSuccess(String txnRef) throws CustomRuntimeException {
        TransactionDto transactionDto = transactionService.updateTransactionStatus(txnRef, String.valueOf(Transaction.TransactionStatus.SUCCESS));
        userService.updateBalance(transactionDto.getUserId(), transactionDto.getAmount());

        return String.format("thanh-toan-thanh-cong?vnp_TxnRef=%s", txnRef);
    }

    private String handleFailure(String txnRef) {
        transactionService.updateTransactionStatus(txnRef, String.valueOf(Transaction.TransactionStatus.FAILED));
        return String.format("thanh-toan-that-bai?vnp_TxnRef=%s", txnRef);
    }
}


