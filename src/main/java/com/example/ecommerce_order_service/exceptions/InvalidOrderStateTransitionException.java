package com.example.ecommerce_order_service.exceptions;

public class InvalidOrderStateTransitionException extends RuntimeException{
    public InvalidOrderStateTransitionException(String message){
        super(message);
    }
}
