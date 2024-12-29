package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public CompletableFuture<List<Category>> getAllCategory() {
        return null;
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
    public CompletableFuture<Void> deleteCategory() {
        return null;
    }
}
