package com.alertsystem.service;

import com.alertsystem.model.Alert;
import com.alertsystem.model.Transaction;
import com.alertsystem.repository.AlertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertProcessingServiceTest {

    @Mock
    AlertRepository alertRepository;

    @Mock
    CacheService cacheService;

    @InjectMocks
    AlertProcessingService service;

    @Captor
    ArgumentCaptor<Alert> alertCaptor;

    @Test
    void transactionAbove10000_generatesHighSeverityAlert() {
        Transaction tx = new Transaction();
        tx.setId("tx-high");
        tx.setAccountId("acc1");
        tx.setAmount(15000);
        tx.setTimestamp(LocalDateTime.now());

        Alert result = service.process(tx);

        assertNotNull(result);
        assertEquals("HIGH", result.getSeverity());
        assertEquals("HIGH_VALUE", result.getAlertType());
        assertEquals("acc1", result.getAccountId());
        assertNotNull(result.getCreatedAt());
        assertFalse(result.isResolved());
    }

    @Test
    void transactionBelow5000_generatesLowSeverityAlert() {
        Transaction tx = new Transaction();
        tx.setId("tx-low");
        tx.setAccountId("acc2");
        tx.setAmount(1000);

        Alert result = service.process(tx);

        assertNotNull(result);
        assertEquals("LOW", result.getSeverity());
        assertEquals("NORMAL", result.getAlertType());
        assertEquals("acc2", result.getAccountId());
    }

    @Test
    void save_resolvedAlert_returnsUpdatedStatus() {
        Alert a = new Alert();
        a.setId("a1");
        a.setResolved(true);
        a.setCreatedAt(LocalDateTime.now());

        when(alertRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Alert saved = service.save(a);

        verify(alertRepository, times(1)).save(alertCaptor.capture());
        verify(cacheService, times(1)).saveAlert(saved);
        Alert captured = alertCaptor.getValue();
        assertTrue(captured.isResolved());
        assertEquals(saved.isResolved(), true);
    }
}
