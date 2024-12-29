package com.eCommerce.product.service;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDto;
import com.eCommerce.product.model.Category;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CategoryService {
    CompletableFuture<List<Category>> getAllCategory();
    CompletableFuture<Category> getCategory(Long id);
    CompletableFuture<Category> addCategory(CategoryDto categoryDto);
    CompletableFuture<Void> modifyName(Long id, ModifyDto modifyDto);
    CompletableFuture<Void> modifyDescription(Long id, ModifyDto modifyDto);
    CompletableFuture<Void> deleteCategory();
}
