package com.example.ecommerce_order_service.exceptions;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message){
        super(message);
    }
}
