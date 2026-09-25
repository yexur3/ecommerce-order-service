package com.example.ecommerce_order_service.services;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.exceptions.InvalidOrderStateTransitionException;
import com.example.ecommerce_order_service.repositories.OrderRepository;
import com.example.ecommerce_order_service.services.simulation.PaymentGatewaySimulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentGatewaySimulator paymentGatewaySimulator;

    public PaymentService(OrderRepository orderRepository, PaymentGatewaySimulator paymentGatewaySimulator){
        this.orderRepository = orderRepository;
        this.paymentGatewaySimulator = paymentGatewaySimulator;
    }

    @Transactional
    public void payForOrder(long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("There is no order with this id: " + orderId));

        if(!order.getStatus().canTransitionTo(Status.PAID)){
            throw new InvalidOrderStateTransitionException("Cannot transition from " + order.getStatus() + " to " + Status.PAID);
        }

        paymentGatewaySimulator.processPayment(order.getTotalAmount());

        order.setStatus(Status.PAID);

        orderRepository.save(order);
        
    }


}
