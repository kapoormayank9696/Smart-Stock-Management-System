package com.smartstock.controller;

import com.smartstock.model.StockRequest;
import com.smartstock.model.StockTransaction;
import com.smartstock.repository.StockTransactionRepository;
import com.smartstock.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService service;
    private final StockTransactionRepository transactions;

    public StockController(StockService service, StockTransactionRepository transactions) {
        this.service = service;
        this.transactions = transactions;
    }

    @GetMapping
    public List<StockTransaction> history() {
        return transactions.findTop100ByOrderByCreatedAtDesc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockTransaction record(@Valid @RequestBody StockRequest req) {
        return service.record(req);
    }
}
