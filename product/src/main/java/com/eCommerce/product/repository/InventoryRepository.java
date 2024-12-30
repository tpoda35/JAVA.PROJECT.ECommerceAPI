package com.eCommerce.product.repository;

import com.eCommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockLessThan(Integer lowStockNum);
}
