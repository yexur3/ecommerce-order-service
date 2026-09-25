package com.example.ecommerce_order_service.integration_tests;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.dto.OrderItemRequest;
import com.example.ecommerce_order_service.dto.OrderRequest;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.entities.Product;
import com.example.ecommerce_order_service.exceptions.InsufficientStockException;
import com.example.ecommerce_order_service.repositories.ProductRepository;
import com.example.ecommerce_order_service.services.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class OrderIntegrationTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCreateOrderFunction(){
        Product product = new Product();
        product.setName("apple");
        product.setPrice(BigDecimal.valueOf(20.99));
        product.setStockQuantity(10);

        productRepository.save(product);

        OrderItemRequest orderItemRequest = new OrderItemRequest();

        orderItemRequest.setProductId(product.getId());
        orderItemRequest.setQuantity(2);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1);
        orderRequest.setItems(List.of(orderItemRequest));

        Order result = orderService.createOrder(orderRequest);

        Order rightVariant = new Order();

        rightVariant.setId(result.getId());
        rightVariant.setUserId(1);
        rightVariant.setStatus(Status.PENDING);
        rightVariant.setCreatedAt(result.getCreatedAt());
        rightVariant.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));


        Product resProd = productRepository.findById(product.getId()).orElseThrow();

        assertAll(
                () -> assertEquals(rightVariant, result),
                () -> assertEquals(product.getStockQuantity() - orderItemRequest.getQuantity(), resProd.getStockQuantity())
        );

    }

    @Test
    void testCreateOrderForException() {
        Product product = new Product();
        product.setName("apple");
        product.setPrice(BigDecimal.valueOf(16.99));
        product.setStockQuantity(10);

        productRepository.save(product);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(product.getId());
        orderItemRequest.setQuantity(20);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1);
        orderRequest.setItems(List.of(orderItemRequest));

        assertThrows(InsufficientStockException.class, () -> {
            orderService.createOrder(orderRequest);
        });
    }

}
