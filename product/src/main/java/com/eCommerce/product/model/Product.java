package com.eCommerce.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private Date addedDate;

    private Date modifiedDate;

    private Integer stock;

    @PrePersist
    protected void onCreate(){
        this.addedDate = new Date();
        this.modifiedDate = new Date();
        this.stock = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = new Date();
    }

    @Version
    private Integer Version;
}
