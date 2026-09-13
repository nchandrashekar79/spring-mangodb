package com.example.springmongodb.product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        Instant createdAt,
        Instant updatedAt) {
}