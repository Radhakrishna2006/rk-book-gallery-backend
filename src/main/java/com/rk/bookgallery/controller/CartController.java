package com.rk.bookgallery.controller;

import com.rk.bookgallery.dto.CartItemRequest;
import com.rk.bookgallery.dto.CartItemResponse;
import com.rk.bookgallery.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public List<CartItemResponse> getCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }

    @PostMapping("/cart")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody CartItemRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(authentication.getName(), request));
    }

    @PutMapping("/cart/{id}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long id,
                                                        @Valid @RequestBody CartItemRequest request,
                                                        Authentication authentication) {
        return ResponseEntity.ok(cartService.updateCartItem(authentication.getName(), id, request));
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id, Authentication authentication) {
        cartService.deleteCartItem(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
