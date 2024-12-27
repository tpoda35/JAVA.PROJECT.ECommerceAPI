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

        return inventoryService.modifyStock(id, modifyDto.newStock())
                .thenApply(result -> ResponseEntity.noContent().build());
    }

    //Modifies the given product name.
    @PatchMapping("/modify-name/{prod-id}")
    public CompletableFuture<ResponseEntity<Void>> modifyName(
            @PathVariable("prod-id") Long id,
            @RequestBody ModifyDto modifyDto
    ){
        logger.info("Received request to /inventory/modify-name/prod-id with the id: {}, and name: {}",
                id, modifyDto.name());

        return inventoryService.modifyName(id, modifyDto.name())
                .thenApply(result -> ResponseEntity.noContent().build());
    }

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Product>> addProduct(
            @RequestBody @Valid ProductDto productDto
    ) {
        logger.info("Received request to /inventory/add with the data: {}",
                productDto);

        return inventoryService.addProduct(productDto)
                .thenApply(product ->
                        ResponseEntity.created(URI.create("/inventory/" + product.getId()))
                                .body(product));
    }

}
