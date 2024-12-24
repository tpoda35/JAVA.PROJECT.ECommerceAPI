package com.eCommerce.product.controller;

import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{prod-id}")
    public CompletableFuture<ResponseEntity<Product>> getProductById(@PathVariable("prod-id") Long id) {
        return inventoryService.getProductById(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/low-stock")
    public CompletableFuture<ResponseEntity<List<Product>>> getLowStockProducts(){
        return null;
    }

}
