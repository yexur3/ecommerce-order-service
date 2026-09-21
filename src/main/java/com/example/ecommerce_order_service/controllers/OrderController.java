package com.example.ecommerce_order_service.controllers;

import com.example.ecommerce_order_service.dto.OrderRequest;
import com.example.ecommerce_order_service.dto.OrderResponse;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable long id){
        return orderService.getOrder(id);
    }

}
