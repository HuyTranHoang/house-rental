package com.project.house.rental.controller;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.service.vnPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final VNPayService vnPayService;

    public VNPayController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentDto> createPayment(@Valid @RequestBody PaymentRequest paymentRequest, HttpServletRequest request) throws IOException {
        PaymentDto payment = vnPayService.createPayment(paymentRequest, request);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/return")
    public ResponseEntity<String> returnPayment(
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef
    ) {
        //Đổi ip 2 url khi chạy trên server
        if (responseCode.equals("00")) {
            String successUrl = "http://localhost:3000/thanh-toan-thanh-cong?vnp_Amount=" + amount + "&vnp_OrderInfo=" + orderInfo + "&vnp_TxnRef=" + txnRef;
            return ResponseEntity.status(302).header("Location", successUrl).build();
        }

        String failureUrl = "http://localhost:3000/thanh-toan-that-bai";
        return ResponseEntity.status(302).header("Location", failureUrl).build();
    }
}


