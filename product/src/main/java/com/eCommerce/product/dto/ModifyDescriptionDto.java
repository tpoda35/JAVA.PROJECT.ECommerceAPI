package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ModifyDescriptionDto(
        @NotBlank
        String description
) { }
