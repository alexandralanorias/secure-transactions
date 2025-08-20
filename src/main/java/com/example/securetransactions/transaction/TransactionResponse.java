package com.example.securetransactions.transaction;

import java.math.BigDecimal;

public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private String description;

    public TransactionResponse(Long id, BigDecimal amount, String description) {
        this.id = id;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}