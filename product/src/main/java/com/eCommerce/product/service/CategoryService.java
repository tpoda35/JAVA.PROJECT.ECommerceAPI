package com.eCommerce.product.service;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDescriptionDto;
import com.eCommerce.product.dto.ModifyNameDto;
import com.eCommerce.product.model.Category;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CategoryService {
    CompletableFuture<List<Category>> getAllCategory();
    CompletableFuture<Category> getCategory(Long id);
    CompletableFuture<Category> addCategory(CategoryDto categoryDto);
    CompletableFuture<CategoryDto> modifyName(Long id, ModifyNameDto modifyDto);
    CompletableFuture<Void> modifyDescription(Long id, ModifyDescriptionDto modifyDto);
    CompletableFuture<Void> deleteCategory(Long id);
}
