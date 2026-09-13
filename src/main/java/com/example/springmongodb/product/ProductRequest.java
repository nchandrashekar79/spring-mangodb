package com.example.springmongodb.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "name is required") String name,
        String description,
        @NotNull(message = "price is required") @DecimalMin(value = "0.01", message = "price must be greater than zero") BigDecimal price) {
}