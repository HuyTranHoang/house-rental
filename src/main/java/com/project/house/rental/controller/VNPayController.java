package com.project.house.rental.controller;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.service.vnPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PaymentDto> createPayment(HttpServletRequest request) throws IOException {
        PaymentDto payment = vnPayService.createPayment(request);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/return")
    public ResponseEntity<String> returnPayment(
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_ResponseCode") String responseCode
    ) {
        if (responseCode.equals("00")) {
            return ResponseEntity.ok("Payment success with amount: " + amount + " and order info: " + orderInfo);
        }

        return ResponseEntity.ok("Payment failed with amount: " + amount + " and order info: " + orderInfo);
    }
}


