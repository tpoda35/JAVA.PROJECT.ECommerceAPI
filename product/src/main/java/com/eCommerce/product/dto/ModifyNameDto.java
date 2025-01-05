package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ModifyNameDto(
        @NotBlank
        String name
) { }
