package com.eCommerce.product.controller;

import com.eCommerce.product.model.Category;
import com.eCommerce.product.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
