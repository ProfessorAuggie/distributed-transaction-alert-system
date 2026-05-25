package com.alertsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Document(collection = "transactions")
public class Transaction {
    @Id
    @NotBlank(message = "Transaction id is required")
    private String id;

    @NotBlank(message = "Account id is required")
    private String accountId;

    @Positive(message = "Amount must be greater than zero")
    private double amount;

    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "CREDIT|DEBIT", message = "Transaction type must be CREDIT or DEBIT")
    private String type; // CREDIT or DEBIT

    @NotBlank(message = "Transaction status is required")
    private String status;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
