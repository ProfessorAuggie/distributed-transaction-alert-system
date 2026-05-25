package com.alertsystem.service;

import com.alertsystem.model.Alert;
import com.alertsystem.model.Transaction;
import com.alertsystem.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AlertProcessingService {

    private final AlertRepository alertRepository;
    private final CacheService cacheService;

    public AlertProcessingService(AlertRepository alertRepository, CacheService cacheService) {
        this.alertRepository = alertRepository;
        this.cacheService = cacheService;
    }

    public Alert process(Transaction tx) {
        Alert a = new Alert();
        a.setTransactionId(tx.getId());
        if (tx.getAmount() > 10000) {
            a.setSeverity("HIGH");
            a.setAlertType("HIGH_VALUE");
        } else if (tx.getAmount() > 5000) {
            a.setSeverity("MEDIUM");
            a.setAlertType("MEDIUM_VALUE");
        } else {
            a.setSeverity("LOW");
            a.setAlertType("NORMAL");
        }
        // set account id from transaction
        a.setAccountId(tx.getAccountId());
        a.setCreatedAt(LocalDateTime.now());
        a.setResolved(false);
        return a;
    }

    public Alert save(Alert alert) {
        if (alert.getCreatedAt() == null) {
            alert.setCreatedAt(LocalDateTime.now());
        }
        Alert saved = alertRepository.save(alert);
        cacheService.saveAlert(saved);
        return saved;
    }
}
