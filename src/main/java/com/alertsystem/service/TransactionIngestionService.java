package com.alertsystem.service;

import com.alertsystem.model.Transaction;
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
        if (transactions == null || transactions.isEmpty()) {
            return;
        }

        List<CompletableFuture<Void>> futures = transactions.stream()
                .map(tx -> CompletableFuture.supplyAsync(() -> alertProcessingService.process(tx), taskExecutor)
                        .thenAccept(alertProcessingService::save))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
