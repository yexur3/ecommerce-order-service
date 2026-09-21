package com.example.ecommerce_order_service.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private long productId;
    private long quantity;
}
