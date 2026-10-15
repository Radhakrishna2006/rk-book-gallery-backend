package com.rk.bookgallery.service;

import com.rk.bookgallery.dto.BookRequest;
import com.rk.bookgallery.entity.Book;
import com.rk.bookgallery.exception.BadRequestException;
import com.rk.bookgallery.exception.ResourceNotFoundException;
import com.rk.bookgallery.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword, keyword, keyword);
    }

    public Book createBook(BookRequest request) {
        if (bookRepository.findAll().stream().anyMatch(book -> book.getIsbn().equalsIgnoreCase(request.getIsbn().trim()))) {
            throw new BadRequestException("Book with this ISBN already exists");
        }

        Book book = new Book();
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setIsbn(request.getIsbn().trim());
        book.setCategory(request.getCategory().trim());
        book.setDescription(request.getDescription().trim());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setPublisher(request.getPublisher());
        book.setPublicationYear(request.getPublicationYear());
        book.setPages(request.getPages());
        book.setImageUrl(request.getImageUrl());
        book.setRating(request.getRating() == null ? 4.5 : request.getRating());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, BookRequest request) {
        Book existing = getBookById(id);
        existing.setTitle(request.getTitle().trim());
        existing.setAuthor(request.getAuthor().trim());
        existing.setIsbn(request.getIsbn().trim());
        existing.setCategory(request.getCategory().trim());
        existing.setDescription(request.getDescription().trim());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setPublisher(request.getPublisher());
        existing.setPublicationYear(request.getPublicationYear());
        existing.setPages(request.getPages());
        existing.setImageUrl(request.getImageUrl());
        existing.setRating(request.getRating() == null ? existing.getRating() : request.getRating());
        return bookRepository.save(existing);
    }

    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    public List<String> getAllCategories() {
        return bookRepository.findAll().stream()
                .map(Book::getCategory)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }
}
