package com.rk.bookgallery.controller;

import com.rk.bookgallery.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final BookService bookService;

    public CategoryController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return bookService.getAllCategories();
    }
}
