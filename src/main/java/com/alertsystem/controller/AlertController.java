package com.alertsystem.controller;

import com.alertsystem.model.Alert;
import com.alertsystem.model.Transaction;
import com.alertsystem.repository.AlertRepository;
import com.alertsystem.service.TransactionIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@RestController
@Validated
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;
    private final TransactionIngestionService ingestionService;

    public AlertController(AlertRepository alertRepository,
                           TransactionIngestionService ingestionService) {
        this.alertRepository = alertRepository;
        this.ingestionService = ingestionService;
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAll() {
        return ResponseEntity.ok(alertRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getById(@PathVariable @NotBlank String id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new com.alertsystem.exception.ResourceNotFoundException("Alert not found", "ALERT_NOT_FOUND"));
        return ResponseEntity.ok(alert);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Alert>> getByAccount(@PathVariable @NotBlank String accountId) {
        return ResponseEntity.ok(alertRepository.findByAccountId(accountId));
    }

    @PostMapping("/ingest")
    public ResponseEntity<Void> ingestTransactions(@RequestBody @NotEmpty List<@Valid Transaction> transactions) {
        ingestionService.ingest(transactions);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable @NotBlank String id) {
        Alert a = alertRepository.findById(id)
                .orElseThrow(() -> new com.alertsystem.exception.ResourceNotFoundException("Alert not found", "ALERT_NOT_FOUND"));
        if (a.isResolved()) {
            throw new com.alertsystem.exception.AlertAlreadyResolvedException("Alert already resolved", "ALERT_ALREADY_RESOLVED");
        }
        a.setResolved(true);
        Alert saved = alertRepository.save(a);
        return ResponseEntity.ok(saved);
    }
}
