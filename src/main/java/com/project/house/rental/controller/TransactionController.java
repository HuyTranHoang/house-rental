package com.project.house.rental.controller;

import com.project.house.rental.dto.ReportDto;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getAllTransactionsWithParams(@ModelAttribute TransactionParams transactionParams) {
        Map<String, Object> transactionsWithParams  = transactionService.getAllTransactionsWithParams(transactionParams);
        return ResponseEntity.ok(transactionsWithParams);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto, HttpServletRequest request) {
        TransactionDto transaction = transactionService.createTransaction(transactionDto, request);
        return  ResponseEntity.ok(transaction);
    }

}