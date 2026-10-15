package com.rk.bookgallery.controller;

import com.rk.bookgallery.dto.UserResponse;
import com.rk.bookgallery.entity.OrderStatus;
import com.rk.bookgallery.repository.UserRepository;
import com.rk.bookgallery.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository userRepository;
    private final OrderService orderService;

    public AdminController(UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    @GetMapping("/orders")
    public Object getOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/orders/{id}/status")
    public Object updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}
