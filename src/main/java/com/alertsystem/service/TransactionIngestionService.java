package com.alertsystem.service;

import com.alertsystem.model.Transaction;
import com.alertsystem.model.Alert;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class TransactionIngestionService {

    private final AlertProcessingService alertProcessingService;
    private final Executor taskExecutor;

    public TransactionIngestionService(AlertProcessingService alertProcessingService,
                                       @Qualifier("taskExecutor") Executor taskExecutor) {
        this.alertProcessingService = alertProcessingService;
        this.taskExecutor = taskExecutor;
    }

    @Async
    public void ingest(List<Transaction> transactions) {
        List<CompletableFuture<Void>> futures = transactions.stream()
                .map(tx -> CompletableFuture.supplyAsync(() -> alertProcessingService.process(tx), taskExecutor)
                        .thenAccept(alertProcessingService::save))
                .collect(Collectors.toList());

        // Optionally wait for all to complete in this async method
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
package com.alertsystem.service;

import com.alertsystem.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionIngestionService {
    public void ingest(Transaction transaction) {
        // placeholder: ingesting transaction (e.g., from Kafka/HTTP)
    }
}
