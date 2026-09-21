package com.example.ecommerce_order_service.services;

import com.example.ecommerce_order_service.dto.OrderItemRequest;
import com.example.ecommerce_order_service.dto.OrderRequest;
import com.example.ecommerce_order_service.dto.OrderResponse;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.entities.OrderItem;
import com.example.ecommerce_order_service.entities.Product;
import com.example.ecommerce_order_service.repositories.OrderItemRepository;
import com.example.ecommerce_order_service.repositories.OrderRepository;
import com.example.ecommerce_order_service.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productsRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productsRepository, OrderItemRepository orderItemRepository){
        this.orderRepository = orderRepository;
        this.productsRepository = productsRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();

        List<OrderItemRequest> items = orderRequest.getItems();

        order.setUserId(orderRequest.getUserId());
        order.setCreatedAt(Instant.now());

        orderRepository.save(order);

        BigDecimal sum = BigDecimal.valueOf(0.0);

        for(var item : items){
            OrderItem orderItem = new OrderItem();
            Product product = productsRepository.findById(item.getProductId()).orElseThrow(
                    () -> new NoSuchElementException("There is no product with: " + item.getProductId())
            );

            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            sum = sum.add(itemTotal);

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(sum);

        orderRepository.save(order);

        return order;
    }

    public OrderResponse getOrder(long id){
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no order with this id: " + id)
        );

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setItems(orderItemRepository.findByOrderId(order.getId()));
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setCreatedAt(order.getCreatedAt());

        return orderResponse;
    }

}
