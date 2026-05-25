package com.alertsystem.controller;

import com.alertsystem.model.Alert;
import com.alertsystem.repository.AlertRepository;
import com.alertsystem.service.TransactionIngestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertControllerTest {

    @Mock
    AlertRepository alertRepository;

    @Mock
    TransactionIngestionService ingestionService;

    @Test
    void ingestTransactions_returnsAccepted() {
        AlertController controller = new AlertController(alertRepository, ingestionService);

        ResponseEntity<Void> resp = controller.ingestTransactions(Collections.emptyList());

        verify(ingestionService).ingest(Collections.emptyList());
        assertEquals(HttpStatus.ACCEPTED, resp.getStatusCode());
        assertNotNull(resp);
    }
}
