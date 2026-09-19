package com.example.ecommerce_order_service.entities;

import com.example.ecommerce_order_service.Enum.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    private Status status = Status.PENDING;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private long totalAmount;

}
