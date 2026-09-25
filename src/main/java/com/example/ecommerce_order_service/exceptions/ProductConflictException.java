package com.example.ecommerce_order_service.exceptions;

public class ProductConflictException extends RuntimeException{
    public ProductConflictException(String message){
        super(message);
    }
}
