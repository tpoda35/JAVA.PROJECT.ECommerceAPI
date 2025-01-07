package com.eCommerce.product.service.impl;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDescriptionDto;
import com.eCommerce.product.dto.ModifyNameDto;
import com.eCommerce.product.mapper.CategoryMapper;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.repository.CategoryRepository;
import com.eCommerce.product.service.CategoryService;
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
        logger.info("Category(id:{}) found with the name of {}.", id, cat.getName());

        return CompletableFuture.completedFuture(cat);
    }

    @Async
    @CacheEvict(
            value = "categories",
            allEntries = true
    )
    @Override
    public CompletableFuture<Category> addCategory(CategoryDto categoryDto) {
        try {
            return CompletableFuture.completedFuture(addCategoryTra(categoryDto));
        } catch (Exception e){
            logger.error("Unexpected error during addCategory: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private Category addCategoryTra(CategoryDto categoryDto){
        logger.info("Adding category with the data of: {}", categoryDto.toString());
        Category category = CategoryMapper.INSTANCE.toNormal(categoryDto);
        return categoryRepository.save(category);
    }

    @Async
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "categories",
                    allEntries = true,
                    condition = "#result != null"
            ),
            @CacheEvict(
                    cacheNames = "category",
                    key = "#id",
                    condition = "#result != null"
            )
    })
    @Override
    public CompletableFuture<CategoryDto> modifyCategoryName(Long id, ModifyNameDto modifyDto) {
        try{
            return CompletableFuture.completedFuture(modifyCategoryNameTra(id, modifyDto));
        } catch (Exception e){
            logger.error("Unexpected error during modifyCategoryName: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private CategoryDto modifyCategoryNameTra(Long id, ModifyNameDto modifyDto){
        logger.info("Modifying category(id:{}) name to: {}", id, modifyDto.name());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with the id of " + id + "not found"));

        category.setName(modifyDto.name());
        return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
    }

    @Async
    @Caching(evict = {
            @CacheEvict(
                    cacheNames = "categories",
                    allEntries = true,
                    condition = "#result != null"
            ),
            @CacheEvict(
                    cacheNames = "category",
                    key = "#id",
                    condition = "#result != null"
            )
    })
    @Override
    public CompletableFuture<CategoryDto> modifyCategoryDescription(Long id, ModifyDescriptionDto modifyDto) {
        try {
            return CompletableFuture.completedFuture(modifyCategoryDescriptionTra(id, modifyDto));
        } catch (Exception e){
            logger.error("Unexpected error during modifyCategoryDescription: {}", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional
    private CategoryDto modifyCategoryDescriptionTra(Long id, ModifyDescriptionDto modifyDto){
        logger.info("Modifying category(id:{}) description to: {}", id, modifyDto.description());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with the id of " + id + "not found"));

        category.setDescription(modifyDto.description());
        return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
    }

    @Override
    public CompletableFuture<Void> deleteCategory(Long id) {
        return null;
    }
}
