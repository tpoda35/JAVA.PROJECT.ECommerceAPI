package com.eCommerce.product.service;

import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.model.Product;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InventoryService {
    CompletableFuture<List<Product>> getLowStockProducts();
    CompletableFuture<Void> modifyStock(Long id, int newStock);
    CompletableFuture<Void> modifyName(Long id, String newName);
    CompletableFuture<Product> addProduct(ProductDto productDto);
}
