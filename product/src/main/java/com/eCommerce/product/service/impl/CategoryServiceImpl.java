package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.repository.CategoryRepository;
import com.eCommerce.product.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Async
    @Cacheable(value = "categories")
    @Override
    public CompletableFuture<List<Category>> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        if (!categories.isEmpty()){
            logger.info("Found {} categories.", categories.size());
            return CompletableFuture.completedFuture(categories);
        }

        logger.info("Found 0 category.");
        return CompletableFuture.failedFuture(
                new EntityNotFoundException("There's no category found.")
        );
    }

    @Async
    @Cacheable(value = "category", key = "#id")
    @Override
    public CompletableFuture<Category> getCategory(Long id) {
        // If .orElseThrow is used, itt will return internal server error, not 404 not found.
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()){
            return CompletableFuture.failedFuture(
                    new EntityNotFoundException("Category not found.")
            );
        }
        Category cat = category.get();
        logger.info("Category found with the name of {}, and id of {}.", cat.getName(), id);

        return CompletableFuture.completedFuture(cat);
    }

    @Override
    public CompletableFuture<Category> addCategory(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public CompletableFuture<Void> modifyName(Long id, ModifyDto modifyDto) {
        return null;
    }

    @Override
    public CompletableFuture<Void> modifyDescription(Long id, ModifyDto modifyDto) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteCategory(Long id) {
        return null;
    }
}
