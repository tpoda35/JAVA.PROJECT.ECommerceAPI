package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyIdDto(
        @NotNull
        Long id
) { }
