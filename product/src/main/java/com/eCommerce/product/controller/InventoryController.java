package com.eCommerce.product.controller;

import com.eCommerce.product.dto.ModifyDto;
import com.eCommerce.product.dto.ProductDto;
import com.eCommerce.product.model.Product;
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
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //Gives back the low-stock products(under 5 stock).
    @GetMapping("/low-stock")
    public CompletableFuture<List<Product>> getLowStockProducts(){
        logger.info("Received request to /inventory/low-stock.");

        return inventoryService.getLowStockProducts()
                .thenApply(products -> {
                   logger.info("Received {} product back from /inventory/low-stock.", products.size());
                   return products;
                });
    }

    //Modifies the given product stock.
    @PatchMapping("/modify-stock/{prod-id}")
    public CompletableFuture<ResponseEntity<Void>> modifyStock(
            @PathVariable("prod-id") Long id,
            @RequestBody ModifyDto modifyDto
            ){
        logger.info("Received request to /inventory/modify-stock/prod-id with the id: {}, and stock: {}",
                id, modifyDto.newStock());

        return inventoryService.modifyProductStock(id, modifyDto.newStock())
                .thenApply(result -> {
                    logger.info("A product stock has been modified.");
                    return ResponseEntity.noContent().build();
                });
    }

    //Modifies the given product name.
    @PatchMapping("/modify-name/{prod-id}")
    public CompletableFuture<ResponseEntity<Void>> modifyName(
            @PathVariable("prod-id") Long id,
            @RequestBody ModifyDto modifyDto
    ){
        logger.info("Received request to /inventory/modify-name/prod-id with the id: {}, and name: {}",
                id, modifyDto.name());

        return inventoryService.modifyProductName(id, modifyDto.name())
                .thenApply(result -> {
                    logger.info("A product name has been modified.");
                    return ResponseEntity.noContent().build();
                });
    }

    //Adds a new product to the database.
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Product>> addProduct(
            @RequestBody @Valid ProductDto productDto
    ) {
        logger.info("Received request to /inventory/add with the data: {}",
                productDto.toString());

        return inventoryService.addProduct(productDto)
                .thenApply(product ->
                {
                    logger.info("A product has been added with the id of {}.", product.getId());
                    return ResponseEntity.created(URI.create("/inventory/" + product.getId()))
                            .body(product);
                });
    }

    //Deletes the product from the database.
    @DeleteMapping("/delete/{prod-id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProduct(
            @PathVariable("prod-id") Long id
    ){
        logger.info("Received request to /inventory/delete with the id: {}",
                id);

        return inventoryService.deleteProduct(id)
                .thenApply(result -> {
                    logger.info("A product has been deleted with the id of {}.", id);
                    return ResponseEntity.noContent().build();
                });
    }
}
