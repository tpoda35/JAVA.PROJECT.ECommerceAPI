package com.eCommerce.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private Date addedDate;

    @Column(nullable = false)
    private Date modifiedDate;

    private Long stock;

    @PrePersist
    protected void onCreate(){
        this.addedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = new Date();
    }

}
