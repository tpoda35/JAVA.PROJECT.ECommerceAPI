package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank
        String name,
        String description
) { }
