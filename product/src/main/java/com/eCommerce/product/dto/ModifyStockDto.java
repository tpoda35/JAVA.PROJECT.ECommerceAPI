package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyStockDto(
        @NotNull
        Integer newStock
) { }
