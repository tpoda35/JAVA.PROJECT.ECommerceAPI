package com.eCommerce.product.service.impl;

import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.InventoryRepository;
import com.eCommerce.product.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Async
    @Override
    public CompletableFuture<Product> getProductById(Long id) {
        return CompletableFuture.completedFuture(inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found.")));
    }
}
