package com.rk.bookgallery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping({"/", "/health"})
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "RK's Book Gallery Backend API",
                "message", "Backend is running smoothly!",
                "timestamp", Instant.now().toString(),
                "frontendUrl", "https://rk-book-gallery.vercel.app",
                "endpoints", Map.of(
                        "books", "/api/books",
                        "categories", "/api/categories",
                        "login", "/api/auth/login",
                        "register", "/api/auth/register"
                )
        ));
    }
}
