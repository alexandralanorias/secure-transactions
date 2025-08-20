package com.example.securetransactions.transaction;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransactionUpdateRequest {
    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount;

    @Size(max = 255, message = "Description too long")
    private String description;

    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
}