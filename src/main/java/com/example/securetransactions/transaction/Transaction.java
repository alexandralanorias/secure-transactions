package com.example.securetransactions.transaction;

import com.example.securetransactions.user.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AppUser owner;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be non-negative")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount;

    @Size(max = 255)
    private String description;

    private Instant createdAt = Instant.now();

    public Transaction() {}

    public Transaction(AppUser owner, BigDecimal amount, String description) {
        this.owner = owner;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() { return id; }
    public AppUser getOwner() { return owner; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setOwner(AppUser owner) { this.owner = owner; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
