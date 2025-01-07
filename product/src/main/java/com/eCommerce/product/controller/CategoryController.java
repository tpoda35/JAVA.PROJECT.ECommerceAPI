package com.eCommerce.product.controller;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyNameDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.CategoryService;
import com.eCommerce.product.service.InventoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final InventoryService inventoryService;

    @Autowired
    public CategoryController(CategoryService categoryService, InventoryService inventoryService) {
        this.categoryService = categoryService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Category>> getAllCategory(){
        logger.info("Received request to /category/all.");

        return categoryService.getAllCategory()
                .thenApply(categories -> {
                    logger.info("Received {} categories back from /category/all.", categories.size());
                    return categories;
                });
    }

    @GetMapping("/{cat-id}")
    public CompletableFuture<Category> getCategory (
            @PathVariable("cat-id") Long id
    ){
        logger.info("Received request to /category/{cat-id}.");
        return categoryService.getCategory(id);
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Category>> addCategory(
            @RequestBody @Valid CategoryDto categoryDto
            ){
        logger.info("Received request to /category/add with the data: {}",
                categoryDto.toString());

        return categoryService.addCategory(categoryDto)
                .thenApply(category -> {
                    logger.info("A category has been added with the id of {}.", category.getId());
                    return ResponseEntity.created(URI.create("/category/" + category.getId()))
                            .body(category);
                });
    }

    @GetMapping("/{cat-id}/all")
    public CompletableFuture<List<Product>> getAllProductByCategory(
            @PathVariable("cat-id") Long categoryId
    ){
        logger.info("Received request to /category/{cat-id}/all with the id of {}.", categoryId);

        return inventoryService.getAllProductByCategory(categoryId)
                .thenApply(products -> {
                    logger.info("Received {} categories back from /inventory/low-stock.", products.size());
                    return products;
                });
    }

    @PatchMapping("/modify-name/{cat-id}")
    public CompletableFuture<CategoryDto> modifyCategoryName(
            @PathVariable("cat-id") Long categoryId,
            @RequestBody @Valid ModifyNameDto modifyDto
            ){
        logger.info("Received request to /category/modify-name/{cat-id} with the id of {}.", categoryId);

        return categoryService.modifyCategoryName(categoryId, modifyDto)
                .thenApply(categoryDto -> {
                    logger.info("Category(id:{}) successfully renamed to {}.", categoryDto, modifyDto.name());
                    return categoryDto;
                });
    }

}
