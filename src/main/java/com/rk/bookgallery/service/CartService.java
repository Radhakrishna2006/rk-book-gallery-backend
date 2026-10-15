package com.rk.bookgallery.service;

import com.rk.bookgallery.dto.CartItemRequest;
import com.rk.bookgallery.dto.CartItemResponse;
import com.rk.bookgallery.entity.Book;
import com.rk.bookgallery.entity.CartItem;
import com.rk.bookgallery.entity.User;
import com.rk.bookgallery.exception.BadRequestException;
import com.rk.bookgallery.exception.ResourceNotFoundException;
import com.rk.bookgallery.repository.BookRepository;
import com.rk.bookgallery.repository.CartItemRepository;
import com.rk.bookgallery.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(CartItemRepository cartItemRepository,
                      BookRepository bookRepository,
                      UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<CartItemResponse> getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartItemRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CartItemResponse addToCart(String email, CartItemRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getStock() <= 0) {
            throw new BadRequestException("Book is out of stock");
        }

        CartItem cartItem = cartItemRepository.findByUserAndBook(user, book)
                .orElseGet(() -> new CartItem(user, book, 0));

        int requestedQuantity = request.getQuantity();
        if (cartItem.getQuantity() + requestedQuantity > book.getStock()) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }

        cartItem.setQuantity(cartItem.getQuantity() + requestedQuantity);
        CartItem saved = cartItemRepository.save(cartItem);
        return toResponse(saved);
    }

    public CartItemResponse updateCartItem(String email, Long cartItemId, CartItemRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to cart item");
        }

        Book book = item.getBook();
        if (request.getQuantity() > book.getStock()) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }

        item.setQuantity(request.getQuantity());
        return toResponse(cartItemRepository.save(item));
    }

    public void deleteCartItem(String email, Long cartItemId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(item);
    }

    private CartItemResponse toResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setBookId(item.getBook().getId());
        response.setTitle(item.getBook().getTitle());
        response.setAuthor(item.getBook().getAuthor());
        response.setImageUrl(item.getBook().getImageUrl());
        response.setPrice(item.getBook().getPrice());
        response.setQuantity(item.getQuantity());
        return response;
    }
}
