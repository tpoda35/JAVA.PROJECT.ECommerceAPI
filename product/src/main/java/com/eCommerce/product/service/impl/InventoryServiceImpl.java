package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.mapper.ProductMapper;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.InventoryRepository;
import com.eCommerce.product.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Async
    @Cacheable(value = "low-stock")
    @Override
    public CompletableFuture<List<Product>> getLowStockProducts() {
        List<Product> lowStock = inventoryRepository.findByStockLessThan(5);

        if (!lowStock.isEmpty()){
            logger.info("Found {} low-stock product.", lowStock.size());
            return CompletableFuture.completedFuture(lowStock);
        }

        logger.info("Found 0 low-stock product.");
        return CompletableFuture.failedFuture(
                new EntityNotFoundException("There's no low stock product found.")
        );
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
            logger.error("Unexpected error during modifyProductStock: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private void modifyStockTra(Long id, int newStock){
        if (newStock < 0){
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        logger.info("Modifying the stock of the product with the id of {}", product.getId());

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
        logger.info("Modifying the name of the product with the id of {}", product.getId());

        product.setName(newName);
        inventoryRepository.save(product);
    }

    @Async
    @Caching(evict = {
            @CacheEvict(cacheNames = "low-stock", allEntries = true),
            @CacheEvict(cacheNames = "prodByCat", key = "#productDto.categoryId")
    })
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
        logger.info("Adding product with the data of: {}", productDto.toString());
        Product product = ProductMapper.INSTANCE.toNormal(productDto);
        return inventoryRepository.save(product);
    }

    // The cache not working. Will be modified.
    // The "prodByCat" cache is not working as expected.
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
        logger.info("Deleting the product with the id of {}.", id);

        inventoryRepository.delete(product);
    }

    @Async
    @Cacheable(value = "prodByCat", key = "#categoryId")
    @Override
    public CompletableFuture<List<Product>> getAllProductByCategory(Long categoryId) {
        List<Product> products = inventoryRepository.findAllByCategoryId(categoryId);

        if (!products.isEmpty()){
            logger.info("Found {} product with the categoryId of {}.", products.size(), categoryId);
            return CompletableFuture.completedFuture(products);
        }

        logger.info("Found 0 product with the categoryId of {}.", categoryId);
        return CompletableFuture.failedFuture(
                new EntityNotFoundException("There's no product found with the categoryId of " + categoryId)
        );
    }
}
