package com.example.ecommerce_order_service.controllers;

import com.example.ecommerce_order_service.services.PaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PutMapping("/{id}")
    public String payForOrder(@PathVariable long id){
        paymentService.payForOrder(id);
        return "Payment successfully done!";
    }

}
