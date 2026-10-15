package com.rk.bookgallery.controller;

import com.rk.bookgallery.dto.OrderRequest;
import com.rk.bookgallery.dto.OrderResponse;
import com.rk.bookgallery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(authentication.getName(), request));
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders(Authentication authentication) {
        return orderService.getOrdersForUser(authentication.getName());
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrderById(@PathVariable Long id, Authentication authentication) {
        return orderService.getOrderById(authentication.getName(), id);
    }
}
