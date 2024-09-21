package com.project.house.rental.controller;

import com.project.house.rental.dto.PaymentDto;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.TransactionService;
import com.project.house.rental.service.vnPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final VNPayService vnPayService;
    private final JWTTokenProvider jwtTokenProvider;

    public TransactionController(TransactionService transactionService, VNPayService vnPayService, JWTTokenProvider jwtTokenProvider) {
        this.transactionService = transactionService;
        this.vnPayService = vnPayService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllTransactionsWithParams(@ModelAttribute TransactionParams transactionParams) {
        Map<String, Object> transactionsWithParams = transactionService.getAllTransactionsWithParams(transactionParams);
        return ResponseEntity.ok(transactionsWithParams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getByTransctionId(@PathVariable String id) {
        TransactionDto transactionDto = transactionService.findByTransactionId(id);
        return ResponseEntity.ok(transactionDto);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> createTransaction(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) throws CustomRuntimeException {
        TransactionDto transaction = transactionService.createTransaction(paymentRequest, request);

        PaymentDto paymentDto = vnPayService.createPayment(paymentRequest, request);

        String paymentUrl = paymentDto.getURL();
        String txnRef = extractTxnRefFromUrl(paymentUrl);
        paymentDto.setTransactionId(txnRef);

        transactionService.updateTransactionId(transaction.getId(), txnRef);

        return ResponseEntity.ok(paymentDto);
    }

    @PutMapping("/status/{txnRef}")
    public ResponseEntity<Void> updateTransactionStatus(@PathVariable String txnRef, @RequestParam String status) {
        transactionService.updateTransactionStatus(txnRef, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user-history")
    public ResponseEntity<Map<String, Object>> getUserTransactionHistory(HttpServletRequest request,@ModelAttribute TransactionParams transactionParams) {
        Map<String, Object> transactions = transactionService.getUserTransactions(request, transactionParams);
        return ResponseEntity.ok(transactions);
    }

    private String extractTxnRefFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    if (pair.length == 2 && "vnp_TxnRef".equals(pair[0])) {
                        return pair[1];
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}