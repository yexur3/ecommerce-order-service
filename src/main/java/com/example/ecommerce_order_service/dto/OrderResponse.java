package com.example.ecommerce_order_service.dto;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.entities.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {

    private long id;

    private long userId;

    private Status status;

    private List<OrderItem> items;

    private BigDecimal totalAmount;

    private Instant createdAt;

}
