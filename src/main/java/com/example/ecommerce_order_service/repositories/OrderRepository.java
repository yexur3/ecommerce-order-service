package com.example.ecommerce_order_service.repositories;

import com.example.ecommerce_order_service.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(long id);
}
