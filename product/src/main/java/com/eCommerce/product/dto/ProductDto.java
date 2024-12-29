package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ProductDto(@NotBlank
                         String name,
                         Date addedDate,
                         Date modifiedDate,
                         Integer stock,
                         @NotNull
                         Long categoryId
) { }
