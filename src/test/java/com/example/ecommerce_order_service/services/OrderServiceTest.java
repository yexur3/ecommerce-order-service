package com.example.ecommerce_order_service.services;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.exceptions.InvalidOrderStateTransitionException;
import com.example.ecommerce_order_service.repositories.OrderItemRepository;
import com.example.ecommerce_order_service.repositories.OrderRepository;
import com.example.ecommerce_order_service.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void changeStatusFromPendingToPaidSucceeds() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Status.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.changeStatus(1L, Status.PAID);

        assertEquals(Status.PAID, result.getStatus());

    }

    @Test
    void changeStatusThrowsWhenTransitionInvalid() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Status.DELIVERED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStateTransitionException.class, () -> {
            orderService.changeStatus(1L, Status.PAID);
        });
    }

}
