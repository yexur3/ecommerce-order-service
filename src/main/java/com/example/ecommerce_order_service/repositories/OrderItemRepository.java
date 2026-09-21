package com.example.ecommerce_order_service.repositories;

import com.example.ecommerce_order_service.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findById(long id);
    List<OrderItem> findByOrderId(long id);
}
