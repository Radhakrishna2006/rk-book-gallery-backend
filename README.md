# RK's Book Gallery — Backend API

[![Live API](https://img.shields.io/badge/Render-Live%20API-46E3B7?logo=render&logoColor=white)](https://rk-book-gallery-backend.onrender.com/api/books)
[![Live Frontend](https://img.shields.io/badge/Vercel-Live%20Frontend-black?logo=vercel&logoColor=white)](https://rk-book-gallery.vercel.app)
[![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

Spring Boot 3 REST API for **RK’s Book Gallery**, an online bookstore web application. Deployed on **Render** with a managed **PostgreSQL** database and containerized via Docker.

---

## 🌐 Live Deployment Links

- **Backend API Base**: [https://rk-book-gallery-backend.onrender.com](https://rk-book-gallery-backend.onrender.com)
- **Sample Endpoint**: [https://rk-book-gallery-backend.onrender.com/api/books](https://rk-book-gallery-backend.onrender.com/api/books)
- **Frontend App**: [https://rk-book-gallery.vercel.app](https://rk-book-gallery.vercel.app)
- **Frontend Repository**: [https://github.com/Radhakrishna2006/rk-book-gallery-frontend](https://github.com/Radhakrishna2006/rk-book-gallery-frontend)

---

## 🔑 Default Admin Account

- **Email**: `admin@rkbookgallery.com`
- **Password**: `Admin@123`

---

## 🚀 Tech Stack

- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Security**: Spring Security 6 with stateless JWT Bearer authentication
- **Persistence**: Spring Data JPA & Hibernate (auto-schema generation & migration)
- **Databases Supported**: PostgreSQL (production on Render) and MySQL (local development)
- **Containerization**: Multi-stage Docker build with Eclipse Temurin Alpine JRE

---

## 📡 API Endpoints

Base path: `/api`

### Public Endpoints
- `GET /api/books` — List all books
- `GET /api/books/{id}` — Get book details
- `GET /api/books/categories` — Get list of available categories
- `POST /api/auth/register` — Create a new customer account
- `POST /api/auth/login` — Sign in and receive a JWT Bearer token

### Customer Endpoints (Bearer Token required)
- `GET /api/cart` — Get current user's shopping cart
- `POST /api/cart` — Add book to cart
- `PUT /api/cart/{cartItemId}` — Update cart item quantity
- `DELETE /api/cart/{cartItemId}` — Remove cart item
- `GET /api/orders` — List user's orders
- `POST /api/orders` — Place a new order

### Admin Endpoints (Bearer Token + `ROLE_ADMIN` required)
- `POST /api/admin/books` — Create a new book
- `PUT /api/admin/books/{id}` — Update book details and stock
- `DELETE /api/admin/books/{id}` — Remove book from catalog
- `GET /api/admin/orders` — View all store orders
- `PUT /api/admin/orders/{id}/status` — Update order delivery status

---

## 💻 Local Development

### Prerequisites
- JDK 17+
- Maven 3+
- MySQL or PostgreSQL running locally

### Environment Variables
```bash
export DB_URL="jdbc:mysql://localhost:3306/bookgallery?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="your-mysql-password"
export JWT_SECRET="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
export ADMIN_SEED_PASSWORD="Admin@123"
export CORS_ALLOWED_ORIGINS="http://localhost:5500,http://127.0.0.1:5500,https://rk-book-gallery.vercel.app"
```

### Build & Run
```bash
mvn clean package -DskipTests
java -jar target/bookgallery-1.0.0.jar
```
Or with Docker:
```bash
docker build -t rk-book-gallery-backend .
docker run -p 8080:8080 -e JWT_SECRET=... -e ADMIN_SEED_PASSWORD=... rk-book-gallery-backend
```
