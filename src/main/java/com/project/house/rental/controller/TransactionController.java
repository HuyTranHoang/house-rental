package com.project.house.rental.controller;

import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
