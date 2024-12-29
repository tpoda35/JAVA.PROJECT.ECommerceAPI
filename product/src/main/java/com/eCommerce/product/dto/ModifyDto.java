package com.eCommerce.product.dto;

public record ModifyDto(
        Integer newStock,
        String name,
        String description
) { }
