package com.project.house.rental.service.vnPay;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import jakarta.servlet.http.HttpServletRequest;


public interface VNPayService {
    PaymentDto createPayment(PaymentRequest paymentRequest, HttpServletRequest req);

}
