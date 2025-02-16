package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.mapper.ProductMapper;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.InventoryRepository;
import com.eCommerce.product.service.InventoryService;
import com.eCommerce.product.util.CacheEvictionUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CacheEvictionUtil cacheEvictionUtil;
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

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
    @Caching(evict = {
            @CacheEvict(cacheNames = "low-stock", allEntries = true),
            @CacheEvict(cacheNames = "prodByCat", key = "#result.categoryId")
    })
    @Override
    public CompletableFuture<ProductDto> modifyProductStock(Long id, int newStock) {
        try {
            return CompletableFuture.completedFuture(modifyStockTra(id, newStock));
        } catch (Exception e) {
            logger.error("Unexpected error during modifyProductStock: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private ProductDto modifyStockTra(Long id, int newStock){
        if (newStock < 0){
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        logger.info("Modifying the stock of the product(id:{}).", product.getId());

        product.setStock(newStock);
        return ProductMapper.INSTANCE.toDto(inventoryRepository.save(product));
    }

    @Async
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "low-stock",
                    allEntries = true
            ),
            @CacheEvict(
                    cacheNames = "prodByCat",
                    key = "#result.categoryId",
                    condition = "#result != null"
            )
    })
    @Override
    public CompletableFuture<ProductDto> modifyProductName(Long id, String newName) {
        try {
            return CompletableFuture.completedFuture(modifyNameTra(id, newName));
        } catch (Exception e){
            logger.error("Unexpected error during modifyProductName: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private ProductDto modifyNameTra(Long id, String newName){
        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        logger.info("Modifying the name of the product(id:{}).", product.getId());

        product.setName(newName);
        return ProductMapper.INSTANCE.toDto(inventoryRepository.save(product));
    }

    @Async
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "low-stock",
                    allEntries = true,
                    condition = "#productDto.stock < 5"
            ),
            @CacheEvict(
                    cacheNames = "prodByCat",
                    key = "#productDto.categoryId")
    })
    @Override
    public CompletableFuture<Product> addProduct(ProductDto productDto) {
        try {
            return CompletableFuture.completedFuture(addProductTra(productDto));
        } catch (Exception e){
            logger.error("Unexpected error during addProduct: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private Product addProductTra(ProductDto productDto){
        logger.info("Adding product with the data of: {}", productDto.toString());
        Product product = ProductMapper.INSTANCE.toNormal(productDto);
        return inventoryRepository.save(product);
    }

    @Async
    @CacheEvict(value = "low-stock",
            allEntries = true)
    @Override
    public CompletableFuture<Void> deleteProduct(Long id) {
        try {
            deleteProductTra(id);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e){
            logger.error("Unexpected error during deleteProduct: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private void deleteProductTra(Long id){
        Product product = inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        logger.info("Deleting the product(id:{}).", id);

        cacheEvictionUtil.evictProdByCatCache(product.getCategoryId());
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

    @Async
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "low-stock",
                    allEntries = true,
                    condition = "#result != null && #result.stock < 5"
            ),
            @CacheEvict(
                    cacheNames = "prodByCat",
                    key = "#result.categoryId",
                    condition = "#result != null"
            )
    })
    @Override
    public CompletableFuture<ProductDto> modifyProductCatId(Long productId, Long newId) {
        try {
            return CompletableFuture.completedFuture(modifyProductCatIdTra(productId, newId));
        } catch (Exception e){
            logger.error("Unexpected error during modifyProductCatId: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private ProductDto modifyProductCatIdTra(Long productId, Long newId){
        Product product = inventoryRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
        logger.info("Modifying categoryId({}) of the product(id:{}).",
                product.getCategoryId(), productId);

        product.setCategoryId(newId);
        return ProductMapper.INSTANCE.toDto(inventoryRepository.save(product));
    }
}
