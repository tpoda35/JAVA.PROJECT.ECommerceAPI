package com.eCommerce.product.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record ProductDto(@NotBlank
                         String name,

                         Date addedDate,

                         Date modifiedDate,

                         Integer stock
) { }
