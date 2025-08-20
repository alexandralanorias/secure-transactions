# Secure Transactions API (Spring Boot, Java 21)

A minimal but secure CRUD API with user authentication and role-based access control.

## Stack
- Java 21, Spring Boot 3.3
- Spring Security (BCrypt password hashing)
- Spring Data JPA (H2 in-memory DB)
- Bean Validation (Jakarta Validation)
- Global exception handling
- Basic XSS hardening for text fields (sanitization + validation)
- Protection against SQL injection via JPA parameter binding (no string concatenation queries)

## Run
```bash
# Requires Java 21 and Maven
mvn spring-boot:run
```

The app starts on `http://localhost:8080`. H2 console is available at `/h2-console` (dev only).

## Authentication
- HTTP Basic over HTTPS (for demo purposes). In production, prefer session or JWT with TLS.
- Default admin user is seeded:
  - username: `admin`
  - password: `AdminPass123!`

## Register
```
POST /auth/register
Content-Type: application/json

{
  "username": "alex",
  "password": "StrongPassw0rd!"
}
```
Returns 200 on success. Then authenticate subsequent requests with HTTP Basic using your username/password.

## Roles & Access
- `ROLE_USER`
  - Create a transaction: `POST /api/transactions`
  - List own transactions: `GET /api/transactions/mine`
  - Update own transaction: `PUT /api/transactions/{id}`
  - Delete own transaction: `DELETE /api/transactions/{id}`
- `ROLE_ADMIN`
  - Everything a USER can do
  - List all transactions: `GET /api/transactions/all`

Method-level rules are enforced with `@PreAuthorize` and a `SecurityFilterChain` configured in `SecurityConfig`.

## Security Choices (Short Rationale)
- **Password hashing**: BCrypt (`BCryptPasswordEncoder`) with a reasonable work factor. Never store plaintext passwords.
- **SQL Injection**: Use Spring Data JPA repositories and parameter binding (e.g., `findByIdAndOwner`, `@Query` with `:params`)—no dynamic string concatenation.
- **XSS**: API returns JSON, which is safer by default. As defense-in-depth we sanitize `description` to strip angle brackets and `script` tags (see `HtmlSanitizer`) and validate max length. In a real production system, adopt a robust HTML sanitizer like *OWASP Java HTML Sanitizer* if you must allow rich text.
- **Input validation**: Use Jakarta Validation annotations on DTOs and Entities (`@NotNull`, `@Size`, `@Digits`, etc.).
- **Error handling**: Centralized `@ControllerAdvice` transforms exceptions and validation errors into consistent JSON.
- **Least privilege**: Per-endpoint access with `@PreAuthorize`. Regular users can only access their own transactions; admins can view all.
- **Transport security**: For demo, HTTP Basic is fine, but deploy behind HTTPS. Consider JWT for stateless APIs.
- **Secrets**: No hardcoded secrets besides a dev-only seeded admin for convenience. Replace in prod and use a real DB + secrets manager.
- **CSRF**: Disabled because this is a stateless JSON API without browser cookies. Enable and configure if you later add cookie-based sessions.

## Example Usage

### 1) Register user
```
POST /auth/register
{"username":"alex","password":"StrongPassw0rd!"}
```

### 2) Create transaction (authenticate as alex)
```
POST /api/transactions
Authorization: Basic base64(alex:StrongPassw0rd!)
{"amount": 123.45, "description":"Lunch <script>alert(1)</script>"}
```
Response JSON will have `description` sanitized to mitigate XSS.

### 3) List my transactions
```
GET /api/transactions/mine
Authorization: Basic base64(alex:StrongPassw0rd!)
```

### 4) Admin lists all
```
GET /api/transactions/all
Authorization: Basic base64(admin:AdminPass123!)
```

## Notes
- Database is in-memory; data resets on restart.
- Replace H2 with Postgres/MySQL in `application.yml` for persistence.
- Add rate-limiting, audit logging, and JWT if building beyond this demo.

---

MIT License
