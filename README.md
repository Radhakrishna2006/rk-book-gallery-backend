# Book Gallery Backend

Spring Boot REST API for RK's Book Gallery. Requires Java 17 or newer, Maven,
and a MySQL server.

## Configure and run locally

From the backend repository root, set the database credentials and required
JWT/admin secrets in PowerShell, then start the API:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
$env:JWT_SECRET = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
$env:ADMIN_SEED_PASSWORD = "choose-a-strong-admin-password"
$env:CORS_ALLOWED_ORIGINS = "http://localhost:5500,http://127.0.0.1:5500,http://localhost:8000,http://127.0.0.1:8000"
mvn spring-boot:run
```

By default, the API listens on port `8080` and connects to
`jdbc:mysql://localhost:3306/bookgallery`; the database is created if the
configured MySQL account has permission. Set `DB_URL`, `SERVER_PORT`, and
`JWT_EXPIRATION_MS` to override those defaults.

`JWT_SECRET` must be a Base64-encoded key with at least 32 random bytes.
`ADMIN_SEED_PASSWORD` is required and is used only when the seed administrator
does not already exist:

- Email: `admin@rkbookgallery.com`
- Password: the value of `ADMIN_SEED_PASSWORD`

Set `CORS_ALLOWED_ORIGINS` to a comma-separated list of exact frontend origins
when deploying. The defaults support common local development server ports.

## API

The API base path is `/api`. Public book, category, registration, and login
endpoints are available without a token. Cart, orders, and admin routes use
JWT authentication; admin routes also require the admin role.
