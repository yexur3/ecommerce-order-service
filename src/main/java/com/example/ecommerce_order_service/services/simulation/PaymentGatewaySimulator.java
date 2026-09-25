package com.example.ecommerce_order_service.services.simulation;

import com.example.ecommerce_order_service.exceptions.PaymentFailedException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentGatewaySimulator {

    @Retryable(
            retryFor = PaymentFailedException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
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
