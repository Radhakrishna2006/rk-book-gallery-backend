# Book Gallery Backend

Spring Boot REST API for RK's Book Gallery. Requires Java 17 or newer and a MySQL server.

## Build for Production

```powershell
# Using IntelliJ's bundled Maven (adjust path if needed):
& "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.2\plugins\maven\lib\maven3\bin\mvn.cmd" clean package "-DskipTests=true"
```

The output JAR will be at: `target/bookgallery-1.0.0.jar`

## Deploy to Production

Set the following environment variables on your hosting platform, then run the JAR:

```powershell
$env:DB_URL        = "jdbc:mysql://<host>:3306/bookgallery?useSSL=true&serverTimezone=UTC"
$env:DB_USERNAME   = "your-db-user"
$env:DB_PASSWORD   = "your-db-password"
$env:JWT_SECRET    = "<base64-encoded-32+-byte-random-string>"
$env:ADMIN_SEED_PASSWORD = "choose-a-strong-admin-password"
$env:CORS_ALLOWED_ORIGINS = "https://your-frontend-domain.com"
java -jar target/bookgallery-1.0.0.jar
```

> **Important:** `JWT_SECRET` must be a Base64-encoded key with at least 32 random bytes.
> Generate one with: `[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))`

## Run Locally

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
$env:JWT_SECRET = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
$env:ADMIN_SEED_PASSWORD = "choose-a-strong-admin-password"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:5500,http://127.0.0.1:5500,http://localhost:8000,http://127.0.0.1:8000"
& "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.2\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

The database `bookgallery` is created automatically if the MySQL user has permission.

Seed admin credentials (created on first run if not existing):
- **Email:** `admin@rkbookgallery.com`
- **Password:** value of `ADMIN_SEED_PASSWORD`

## API

Base path: `/api`

| Route type | Auth required |
|---|---|
| Books, categories, register, login | None |
| Cart, orders | JWT Bearer token |
| Admin routes (`/admin/*`) | JWT Bearer token + ADMIN role |
