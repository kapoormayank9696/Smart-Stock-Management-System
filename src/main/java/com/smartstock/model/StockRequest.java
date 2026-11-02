package com.smartstock.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockRequest(
        @NotNull(message = "Product is required") Long productId,
        @NotNull(message = "Type (IN/OUT) is required") StockTransaction.Type type,
        @Min(value = 1, message = "Quantity must be at least 1") int quantity,
        String note) {
}
