package com.example.ecommerce_order_service.services;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.exceptions.PaymentFailedException;
import com.example.ecommerce_order_service.repositories.OrderRepository;
import com.example.ecommerce_order_service.services.simulation.PaymentGatewaySimulator;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentGatewaySimulator paymentGatewaySimulator;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void payForOrderSucceedsWhenPaymentSucceeds(){
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Status.PENDING);
        order.setCreatedAt(Instant.now());
        order.setTotalAmount(BigDecimal.valueOf(100));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        paymentService.payForOrder(1L);

        assertEquals(Status.PAID, order.getStatus());
    }

    @Test
    void payForOrderThrowsPaymentFails() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(Status.PENDING);
        order.setCreatedAt(Instant.now());
        order.setTotalAmount(BigDecimal.valueOf(100));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doThrow(new PaymentFailedException("Payment failed"))
                .when(paymentGatewaySimulator).processPayment(any(BigDecimal.class));


        assertAll(
                () -> assertThrows(PaymentFailedException.class, () -> {
                    paymentService.payForOrder(1L);
                }),
                () -> assertEquals(Status.PENDING, order.getStatus())
        );
    }

}
