package com.rk.bookgallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class BookGalleryApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookGalleryApplication.class, args);
    }
}
