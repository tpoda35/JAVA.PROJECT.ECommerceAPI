package com.eCommerce.product.controller;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.service.CategoryService;
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

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Category>> getAllCategory(){
        logger.info("Received request to /category/all.");

        return categoryService.getAllCategory()
                .thenApply(categories -> {
                    logger.info("Received {} categories back from /inventory/low-stock.", categories.size());
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

}
