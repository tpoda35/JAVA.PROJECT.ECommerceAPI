package com.eCommerce.product.controller;

import com.eCommerce.product.dto.ModifyIdDto;
import com.eCommerce.product.dto.ModifyNameDto;
import com.eCommerce.product.dto.ModifyStockDto;
import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Gives back all the low-stock product(under 5 stock).")
    @ApiResponse(responseCode = "404", description = "There's no low-stock product.")
    @ApiResponse(responseCode = "200", description = "Found low-stock products.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @GetMapping("/low-stock")
    public CompletableFuture<List<Product>> getLowStockProducts(){
        logger.info("Received request to /inventory/low-stock.");

        return inventoryService.getLowStockProducts()
                .thenApply(products -> {
                   logger.info("Received {} product back from /inventory/low-stock.", products.size());
                   return products;
                });
    }

    @Operation(summary = "Modifies the given product stock.")
    @ApiResponse(responseCode = "404", description = "Product not found.")
    @ApiResponse(responseCode = "200", description = "Product stock successfully modified.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PatchMapping("/modify-stock/{prod-id}")
    public CompletableFuture<ProductDto> modifyProductStock(
            @PathVariable("prod-id") Long id,
            @RequestBody @Valid ModifyStockDto modifyDto
            ){
        logger.info("Received request to /inventory/modify-stock/{prod-id} with the id: {}, and stock: {}",
                id, modifyDto.num());

        return inventoryService.modifyProductStock(id, modifyDto.num())
                .thenApply(result -> {
                    logger.info("A product(id:{}) stock has been modified to {}.", id, modifyDto.num());
                    return result;
                });
    }

    @Operation(summary = "Modifies the given product name.")
    @ApiResponse(responseCode = "404", description = "Product not found.")
    @ApiResponse(responseCode = "200", description = "Product name successfully modified.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PatchMapping("/modify-name/{prod-id}")
    public CompletableFuture<ProductDto> modifyProductName(
            @PathVariable("prod-id") Long id,
            @RequestBody @Valid ModifyNameDto modifyDto
    ){
        logger.info("Received request to /inventory/modify-name/{prod-id} with the id: {}, and name: {}",
                id, modifyDto.name());

        return inventoryService.modifyProductName(id, modifyDto.name())
                .thenApply(productDto -> {
                    logger.info("A product(id:{}) name has been modified to {}.", id, modifyDto.name());
                    return productDto;
                });
    }

    @Operation(summary = "Creates a new product.")
    @ApiResponse(responseCode = "201", description = "Product successfully created.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Product>> addProduct(
            @RequestBody @Valid ProductDto productDto
    ) {
        logger.info("Received request to /inventory/add with the data: {}",
                productDto.toString());

        return inventoryService.addProduct(productDto)
                .thenApply(product ->
                {
                    logger.info("A product(id:{}) has been created..", product.getId());
                    return ResponseEntity.created(URI.create("/inventory/" + product.getId()))
                            .body(product);
                });
    }

    @Operation(summary = "Deletes a product.")
    @ApiResponse(responseCode = "404", description = "Product not found.")
    @ApiResponse(responseCode = "204", description = "Product successfully deleted.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @DeleteMapping("/delete/{prod-id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProduct(
            @PathVariable("prod-id") Long id
    ){
        logger.info("Received request to /inventory/delete with the id: {}",
                id);

        return inventoryService.deleteProduct(id)
                .thenApply(result -> {
                    logger.info("A product(id:{}) has been deleted.", id);
                    return ResponseEntity.noContent().build();
                });
    }

    @Operation(summary = "Modifies the given product categoryId.")
    @ApiResponse(responseCode = "404", description = "Product not found.")
    @ApiResponse(responseCode = "200", description = "Product categoryId successfully changed.")
    @ApiResponse(responseCode = "400", description = "Invalid data.")
    @ApiResponse(responseCode = "500", description = "Internal server error occurred.")
    @PatchMapping("/modifyProductCatId/{prod-id}")
    public CompletableFuture<ProductDto> modifyProductCatId(
            @PathVariable("prod-id") Long id,
            @RequestBody @Valid ModifyIdDto modifyDto
    ){
        logger.info("Received request to /inventory/modifyProductCatId/{prod-id} with the id: {}",
                id);

        return inventoryService.modifyProductCatId(id, modifyDto.id())
                .thenApply( productDto -> {
                    logger.info("A product(id:{}) categoryId has been modified.", id);
                    return productDto;
                });
    }
}
