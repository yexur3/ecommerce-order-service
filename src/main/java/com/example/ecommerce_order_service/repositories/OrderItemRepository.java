package com.example.ecommerce_order_service.repositories;

import com.example.ecommerce_order_service.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findById(long id);
}
