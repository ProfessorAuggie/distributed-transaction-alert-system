package com.alertsystem.service;

import com.alertsystem.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionIngestionServiceTest {

    @Mock
    AlertProcessingService alertProcessingService;

    @Test
    void ingest_emptyList_returnsWithoutProcessing() {
        Executor syncExecutor = Runnable::run;
        TransactionIngestionService svc = new TransactionIngestionService(alertProcessingService, syncExecutor);

        svc.ingest(Collections.emptyList());

        verifyNoInteractions(alertProcessingService);
    }
}
