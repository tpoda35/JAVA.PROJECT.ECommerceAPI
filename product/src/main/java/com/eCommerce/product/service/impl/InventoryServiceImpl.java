package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.mapper.ProductMapper;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.InventoryRepository;
import com.eCommerce.product.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Async
    @Cacheable(value = "low-stock")
    @Override
    public CompletableFuture<List<Product>> getLowStockProducts() {
        List<Product> lowStock = inventoryRepository.findByStockLessThan(5)
                .orElseThrow(() -> new EntityNotFoundException("There's no product with low stock(<5)."));

        return CompletableFuture.completedFuture(lowStock);
    }

    @Async
    @CacheEvict(value = "low-stock",
            allEntries = true) // Later add some conditions with SpEL.
    @Override
    public CompletableFuture<Void> modifyProductStock(Long id, int newStock) {
        try {
            modifyStockTra(id, newStock);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private void modifyStockTra(Long id, int newStock){
        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));

        product.setStock(newStock);
        inventoryRepository.save(product);
    }

    @Async
    @CacheEvict(value = "low-stock",
            allEntries = true) // Later add some conditions with SpEL.
    @Override
    public CompletableFuture<Void> modifyProductName(Long id, String newName) {
        try {
            modifyNameTra(id, newName);

            return CompletableFuture.completedFuture(null);
        } catch (Exception e){
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private void modifyNameTra(Long id, String newName){
        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));

        product.setName(newName);
        inventoryRepository.save(product);
    }

    @Async
    @CacheEvict(value = "low-stock",
            allEntries = true) // Later add some conditions with SpEL.
    @Override
    public CompletableFuture<Product> addProduct(ProductDto productDto) {
        try {
            return CompletableFuture.completedFuture(addProductTra(productDto));
        } catch (Exception e){
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private Product addProductTra(ProductDto productDto){
        Product product = ProductMapper.INSTANCE.toNormal(productDto);
        return inventoryRepository.save(product);
    }

    @Async
    @CacheEvict(value = "low-stock",
            allEntries = true) // Later add some conditions with SpEL.
    @Override
    public CompletableFuture<Void> deleteProduct(Long id) {
        try {
            deleteProductTra(id);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e){
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private void deleteProductTra(Long id){
        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        inventoryRepository.delete(product);
    }
}
