package com.example.ecommerce_order_service.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long orderId;

    @Column(nullable = false)
    private long productId;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private double priceAtPurchase;

}
