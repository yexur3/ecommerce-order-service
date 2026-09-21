package com.example.ecommerce_order_service.repositories;

import com.example.ecommerce_order_service.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(long id);
}
