package com.project.house.rental.controller;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.auth.UserEntityDto;
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

    @Value("${base.url}")
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
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef
    ) throws CustomRuntimeException {

        if (responseCode.equals("00")) {
            String successUrl = baseUrl + "thanh-toan-thanh-cong?vnp_Amount=" + amount + "&vnp_OrderInfo=" + orderInfo + "&vnp_TxnRef=" + txnRef;

            TransactionDto transactionDto = transactionService.updateTransactionStatus(txnRef, "SUCCESS");

            UserEntityDto userEntityDto = userService.getUserById(transactionDto.getUserId());
            Double newBalance = userEntityDto.getBalance() + transactionDto.getAmount();
            userService.updateBalance(userEntityDto.getId(), newBalance);

            return ResponseEntity.status(302).header("Location", successUrl).build();
        }

        transactionService.updateTransactionStatus(txnRef, "FAILED");
        String failureUrl = baseUrl + "thanh-toan-that-bai";
        return ResponseEntity.status(302).header("Location", failureUrl).build();
    }
}


