package com.project.house.rental.service.vnPay;

import com.project.house.rental.dto.PaymentDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface VNPayService {
    PaymentDto createPayment(HttpServletRequest req) throws IOException;
}
