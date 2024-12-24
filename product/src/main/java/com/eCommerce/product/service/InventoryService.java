package com.eCommerce.product.service;

import com.eCommerce.product.model.Product;

import java.util.concurrent.CompletableFuture;

public interface InventoryService {
    CompletableFuture<Product> getProductById(Long id);
}
