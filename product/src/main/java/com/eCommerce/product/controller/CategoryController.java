package com.eCommerce.product.controller;

import com.eCommerce.product.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @GetMapping("/all")
    public CompletableFuture<Category> getAll(){
        return null;
    }

}
