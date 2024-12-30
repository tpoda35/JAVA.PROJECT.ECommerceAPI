package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.repository.CategoryRepository;
import com.eCommerce.product.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CategoryServiceImpl implements CategoryService {

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

        if (categories.isEmpty()){
            return CompletableFuture.failedFuture(
                    new EntityNotFoundException("There's no category found.")
            );
        }

        return CompletableFuture.completedFuture(categories);
    }

    @Override
    public CompletableFuture<Category> getCategory(Long id) {
        return null;
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
