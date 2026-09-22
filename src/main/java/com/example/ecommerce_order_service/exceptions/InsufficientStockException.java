package com.example.ecommerce_order_service.exceptions;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(String message){
        super(message);
    }

}
