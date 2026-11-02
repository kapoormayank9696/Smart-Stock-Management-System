package com.smartstock.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "SKU is required") String sku,
        @NotBlank(message = "Name is required") String name,
        String description,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.0", message = "Price cannot be negative") BigDecimal price,
        @Min(value = 0, message = "Quantity cannot be negative") int quantity,
        @Min(value = 0, message = "Reorder level cannot be negative") int reorderLevel,
        Long categoryId,
        Long supplierId) {
}
