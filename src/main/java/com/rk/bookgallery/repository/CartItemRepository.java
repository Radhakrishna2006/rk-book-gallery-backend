package com.rk.bookgallery.repository;

import com.rk.bookgallery.entity.Book;
import com.rk.bookgallery.entity.CartItem;
import com.rk.bookgallery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndBook(User user, Book book);
    void deleteByUser(User user);
}
