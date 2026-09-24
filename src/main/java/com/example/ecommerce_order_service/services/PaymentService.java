package com.example.ecommerce_order_service.services;

import com.example.ecommerce_order_service.Enum.Status;
import com.example.ecommerce_order_service.entities.Order;
import com.example.ecommerce_order_service.exceptions.InvalidOrderStateTransitionException;
import com.example.ecommerce_order_service.exceptions.PaymentFailedException;
import com.example.ecommerce_order_service.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void payForOrder(long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("There is no order with this id: " + orderId));

        if(!order.getStatus().canTransitionTo(Status.PAID)){
            throw new InvalidOrderStateTransitionException("Cannot transition from " + order.getStatus() + " to " + Status.PAID);
        }

        processPayment(order.getTotalAmount());

        order.setStatus(Status.PAID);

        orderRepository.save(order);
        
    }

    public void processPayment(BigDecimal totalAmount) {

        int random = (int)(Math.random() * 101);

        try{

            Thread.sleep(2500);

        } catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }


        if(random <= 20){
            throw new PaymentFailedException("Payment failed, try again later");
        }

    }

}
