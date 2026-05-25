package com.alertsystem.controller;

import com.alertsystem.model.Alert;
import com.alertsystem.model.Transaction;
import com.alertsystem.repository.AlertRepository;
import com.alertsystem.service.TransactionIngestionService;
import com.alertsystem.service.AlertProcessingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;
    private final TransactionIngestionService ingestionService;
    private final AlertProcessingService alertProcessingService;

    public AlertController(AlertRepository alertRepository,
                           TransactionIngestionService ingestionService,
                           AlertProcessingService alertProcessingService) {
        this.alertRepository = alertRepository;
        this.ingestionService = ingestionService;
        this.alertProcessingService = alertProcessingService;
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAll() {
        return ResponseEntity.ok(alertRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getById(@PathVariable String id) {
        Optional<Alert> a = alertRepository.findById(id);
        return a.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Alert>> getByAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(alertRepository.findByAccountId(accountId));
    }

    @PostMapping("/ingest")
    public ResponseEntity<Void> ingestTransactions(@RequestBody List<Transaction> transactions) {
        ingestionService.ingest(transactions);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Alert> resolveAlert(@PathVariable String id) {
        Optional<Alert> o = alertRepository.findById(id);
        if (o.isEmpty()) return ResponseEntity.notFound().build();
        Alert a = o.get();
        if (a.isResolved()) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        a.setResolved(true);
        Alert saved = alertRepository.save(a);
        return ResponseEntity.ok(saved);
    }
}
