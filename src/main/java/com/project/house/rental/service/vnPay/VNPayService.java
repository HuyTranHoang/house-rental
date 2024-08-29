package com.project.house.rental.service.vnPay;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface VNPayService {
    PaymentDto createPayment(PaymentRequest paymentRequest, HttpServletRequest req) throws IOException;

}
