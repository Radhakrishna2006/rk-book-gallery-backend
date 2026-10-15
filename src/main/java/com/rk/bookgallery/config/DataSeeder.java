package com.rk.bookgallery.config;

import com.rk.bookgallery.entity.Book;
import com.rk.bookgallery.entity.User;
import com.rk.bookgallery.entity.UserRole;
import com.rk.bookgallery.repository.BookRepository;
import com.rk.bookgallery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final Map<String, String> SAMPLE_BOOK_COVER_URLS = Map.of(
            "The Silent Horizon", "https://covers.openlibrary.org/b/isbn/9780061122415-L.jpg",
            "The Comic Quest", "https://covers.openlibrary.org/b/isbn/9781506711980-L.jpg",
            "Rise of a Dreamer", "https://covers.openlibrary.org/b/isbn/9781501137969-L.jpg",
            "The Last Orchard", "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg"
    );

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    public DataSeeder(UserRepository userRepository, BookRepository bookRepository, PasswordEncoder passwordEncoder,
                      @Value("${app.seed.admin-password}") String adminPassword) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@rkbookgallery.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@rkbookgallery.com");
            admin.setPhone("9123456780");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);
        }

        if (bookRepository.count() == 0) {
            Book book1 = new Book();
            book1.setTitle("The Silent Horizon");
            book1.setAuthor("Aarav Mehta");
            book1.setIsbn("978-1-234567-01-1");
            book1.setCategory("Stories");
            book1.setDescription("A moving tale of hope, memory, and rediscovery set against an endless sea of silence.");
            book1.setPrice(499);
            book1.setStock(25);
            book1.setPublisher("Blue Pine Press");
            book1.setPublicationYear(2023);
            book1.setPages(310);
            book1.setImageUrl(SAMPLE_BOOK_COVER_URLS.get(book1.getTitle()));
            book1.setRating(4.8);
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("The Comic Quest");
            book2.setAuthor("Naina Kapoor");
            book2.setIsbn("978-1-234567-02-2");
            book2.setCategory("Comics");
            book2.setDescription("Adventure-filled pages with wit, courage, and a comic edge that keeps readers smiling.");
            book2.setPrice(399);
            book2.setStock(18);
            book2.setPublisher("Pixel Chapter");
            book2.setPublicationYear(2024);
            book2.setPages(190);
            book2.setImageUrl(SAMPLE_BOOK_COVER_URLS.get(book2.getTitle()));
            book2.setRating(4.6);
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("Rise of a Dreamer");
            book3.setAuthor("Ritika Saini");
            book3.setIsbn("978-1-234567-03-3");
            book3.setCategory("Biographies");
            book3.setDescription("An inspiring life journey from humble beginnings to extraordinary achievements.");
            book3.setPrice(599);
            book3.setStock(12);
            book3.setPublisher("Northlight Stories");
            book3.setPublicationYear(2022);
            book3.setPages(420);
            book3.setImageUrl(SAMPLE_BOOK_COVER_URLS.get(book3.getTitle()));
            book3.setRating(4.9);
            bookRepository.save(book3);

            Book book4 = new Book();
            book4.setTitle("The Last Orchard");
            book4.setAuthor("Vikram Shah");
            book4.setIsbn("978-1-234567-04-4");
            book4.setCategory("Novels");
            book4.setDescription("A poignant family saga exploring love, loss, and the quiet strength of renewal.");
            book4.setPrice(650);
            book4.setStock(20);
            book4.setPublisher("Golden Leaf");
            book4.setPublicationYear(2024);
            book4.setPages(360);
            book4.setImageUrl(SAMPLE_BOOK_COVER_URLS.get(book4.getTitle()));
            book4.setRating(4.7);
            bookRepository.save(book4);
        }

        bookRepository.findAll().forEach(book -> {
            String sampleCoverUrl = SAMPLE_BOOK_COVER_URLS.get(book.getTitle());
            String currentImageUrl = book.getImageUrl();
            if (sampleCoverUrl != null && (currentImageUrl == null
                    || currentImageUrl.isBlank()
                    || currentImageUrl.startsWith("https://images.unsplash.com/"))) {
                book.setImageUrl(sampleCoverUrl);
                bookRepository.save(book);
            }
        });
    }
}
