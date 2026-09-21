package com.example.ecommerce_order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private long userId;
    private List<OrderItemRequest> items;
}
