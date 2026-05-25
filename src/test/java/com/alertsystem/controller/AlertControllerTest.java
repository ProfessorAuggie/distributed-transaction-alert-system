package com.alertsystem.controller;

import com.alertsystem.model.Transaction;
import com.alertsystem.service.AlertProcessingService;
import com.alertsystem.model.Alert;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AlertControllerTest {

    @Test
    void ingestTransaction_returnsAlert() {
        AlertProcessingService svc = new AlertProcessingService();
        AlertController controller = new AlertController(svc);
        Transaction tx = new Transaction();
        tx.setId("t1");
        tx.setAmount(50);
        ResponseEntity<Alert> resp = controller.ingestTransaction(tx);
        assertNotNull(resp.getBody());
        assertEquals("LOW", resp.getBody().getSeverity());
    }
}
