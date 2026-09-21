package com.example.ecommerce_order_service.controllers;

import com.example.ecommerce_order_service.dto.ProductRequest;
import com.example.ecommerce_order_service.entities.Product;
import com.example.ecommerce_order_service.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public Product createNewProduct(@RequestBody ProductRequest productRequest){
        return productService.createNewProduct(productRequest);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProduct(@PathVariable long id){
        return productService.getProduct(id);
    }

}
