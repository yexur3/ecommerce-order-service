package com.example.ecommerce_order_service.dto;

import com.example.ecommerce_order_service.Enum.Status;
import lombok.Data;

@Data
public class NewStatusRequest {
    private Status status;
}
