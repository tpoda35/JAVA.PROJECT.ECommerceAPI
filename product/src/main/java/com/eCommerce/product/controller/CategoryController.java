package com.eCommerce.product.controller;

import com.eCommerce.product.dto.CategoryDto;
import com.eCommerce.product.dto.ModifyDescriptionDto;
import com.eCommerce.product.dto.ModifyNameDto;
import com.eCommerce.product.model.Category;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.CategoryService;
import com.eCommerce.product.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final InventoryService inventoryService;

    @Operation(summary = "Gives back all the category.")
    @ApiResponse(responseCode = "404", description = "There's no existing category.")
    @ApiResponse(responseCode = "200", description = "Found categories.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @GetMapping("/all")
    public CompletableFuture<List<Category>> getAllCategory(){
        logger.info("Received request to /category/all.");

        return categoryService.getAllCategory()
                .thenApply(categories -> {
                    logger.info("Received {} categories back from /category/all.", categories.size());
                    return categories;
                });
    }

    @Operation(summary = "Gives back the given category.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @ApiResponse(responseCode = "200", description = "Found the given category.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @GetMapping("/{cat-id}")
    public CompletableFuture<Category> getCategory (
            @PathVariable("cat-id") Long id
    ){
        logger.info("Received request to /category/{cat-id}.");
        return categoryService.getCategory(id);
    }

    @Operation(summary = "Creates a new category.")
    @ApiResponse(responseCode = "201", description = "Category successfully created.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
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

    @Operation(summary = "Gives back all the product which is connected to the given category.")
    @ApiResponse(responseCode = "404", description = "There's no product found related to that category.")
    @ApiResponse(responseCode = "200", description = "Found products.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @GetMapping("/{cat-id}/all")
    public CompletableFuture<List<Product>> getAllProductByCategory(
            @PathVariable("cat-id") Long id
    ){
        logger.info("Received request to /category/{cat-id}/all with the id of {}.", id);

        return inventoryService.getAllProductByCategory(id)
                .thenApply(products -> {
                    logger.info("Received {} categories back from /inventory/low-stock.", products.size());
                    return products;
                });
    }

    @Operation(summary = "Modifies the given category name.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @ApiResponse(responseCode = "200", description = "Category name successfully changed.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PatchMapping("/modify-name/{cat-id}")
    public CompletableFuture<CategoryDto> modifyCategoryName(
            @PathVariable("cat-id") Long id,
            @RequestBody @Valid ModifyNameDto modifyDto
            ){
        logger.info("Received request to /category/modify-name/{cat-id} with the id of {}.", id);

        return categoryService.modifyCategoryName(id, modifyDto)
                .thenApply(categoryDto -> {
                    logger.info("Category(id:{}) successfully renamed to {}.", categoryDto, modifyDto.name());
                    return categoryDto;
                });
    }

    @Operation(summary = "Modifies the given category description.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @ApiResponse(responseCode = "200", description = "Category description successfully changed.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PatchMapping("/modify-desc/{cat-id}")
    public CompletableFuture<CategoryDto> modifyCategoryDescription(
            @PathVariable("cat-id") Long id,
            @RequestBody @Valid ModifyDescriptionDto modifyDto
            ){
        logger.info("Received request to /category/modify-desc/{cat-id} with the id of {}.", id);

        return categoryService.modifyCategoryDescription(id, modifyDto)
                .thenApply(categoryDto -> {
                    logger.info("Category(id:{}) description successfully modified to {}.",
                            id, modifyDto.description());
                    return categoryDto;
                });
    }

    @DeleteMapping("/delete/{cat-id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCategory(
            @PathVariable("cat-id") Long id
    ) {
        logger.info("Received request to /category/delete/{cat-id} with the id of {}.", id);

        return categoryService.deleteCategory(id)
                .thenApply(aVoid -> {
                    logger.info("Category(id:{}) description successfully deleted.",
                            id);
                    return ResponseEntity.noContent().build();
                });
    }

    
}
