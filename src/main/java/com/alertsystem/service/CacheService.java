package com.alertsystem.service;

import com.alertsystem.model.Alert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public CacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void saveAlert(Alert alert) {
        try {
            String key = "alert:" + alert.getId();
            String json = objectMapper.writeValueAsString(alert);
            redisTemplate.opsForValue().set(key, json, Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize alert", e);
        }
    }

    public Alert getAlert(String id) {
        String key = "alert:" + id;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, Alert.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize alert", e);
        }
    }

    public List<Alert> getRecentAlerts() {
        Set<String> keys = redisTemplate.keys("alert:*");
        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) return Collections.emptyList();

        List<Alert> alerts = new ArrayList<>();
        for (String json : values) {
            if (json == null) continue;
            try {
                Alert a = objectMapper.readValue(json, Alert.class);
                alerts.add(a);
            } catch (JsonProcessingException ignored) {
            }
        }

        // Sort by createdAt descending and limit to last 20
        return alerts.stream()
                .sorted(Comparator.comparing(Alert::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }
}

