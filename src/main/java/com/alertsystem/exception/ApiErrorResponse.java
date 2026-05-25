package com.alertsystem.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        String errorCode,
        String message,
        String path,
        List<String> details
) {
}
