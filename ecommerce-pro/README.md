# Ecommerce Pro — Spring Boot Backend

A portfolio-grade REST API for an online store built with Java 17, Spring Boot, Spring Security, JWT, MySQL, Redis, Stripe Checkout, Docker Compose, Flyway, OpenAPI, and automated tests.

> This repository is a substantial starter implementation. Run the build and follow the verification checklist before describing it as production-ready. Payment webhook and inventory concurrency hardening are particularly important before deployment.

## Features

- MySQL-backed product and category catalog; no FakeStore product dependency.
- Registration, login, signed JWT access/refresh tokens, BCrypt password hashing, role-based API protection.
- Product search, category filter, sorting, pagination, soft delete, and Redis caching.
- User cart and server-side cart totals.
- Orders and Stripe Checkout test-mode integration with a signed webhook endpoint.
- Flyway schema migrations, OpenAPI UI, Actuator health, Docker Compose, JUnit/Mockito tests.
- DTO validation, centralized API errors, database indexes, optimistic version column on products.

## Requirements

- Docker Desktop + Docker Compose (recommended), or Java 17+, Maven 3.9+, MySQL 8+, Redis 7+.
- Stripe test keys only if you want to exercise real Checkout sessions and webhooks.

## Run with Docker

1. Copy `.env.example` to `.env`.
2. Change all example passwords and set a long random `JWT_SECRET` (at least 32 bytes).
3. For Stripe, set `STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET` from your Stripe test dashboard. Leave blank to run without live checkout; checkout then returns a demo pending-payment response.
4. Run:

   ```bash
   docker compose up --build
   ```

5. Open Swagger UI at http://localhost:8080/swagger-ui.html and health at http://localhost:8080/actuator/health.

Do not use the example secrets or passwords outside local development. Do not commit `.env`.

## Run locally

Start MySQL and Redis, set `DB_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`, `REDIS_HOST`, and `JWT_SECRET`, then run:

```bash
mvn clean test
mvn spring-boot:run
```

## API quick start

### Register

`POST /api/auth/register`

```json
{
  "email": "user@example.com",
  "password": "a-long-demo-password",
  "fullName": "Demo User"
}
```

### Login

`POST /api/auth/login`

```json
{"email":"user@example.com","password":"a-long-demo-password"}
```

Send the returned access token on protected requests:

`Authorization: Bearer <accessToken>`

### Admin bootstrap

For safety, public registration always creates `USER`. To create the first admin, register a user and update its `role` to `ADMIN` directly in your local database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'user@example.com';
```

Never expose this bootstrap procedure as a public endpoint.

### Create a category (ADMIN)

`POST /api/categories`

```json
{"name":"Books","description":"Books and learning"}
```

### Create a product (ADMIN)

`POST /api/products`

```json
{
  "name":"Effective Java",
  "description":"Java programming book",
  "price":499.00,
  "stock":20,
  "imageUrl":"https://example.com/book.jpg",
  "categoryId":1
}
```

### Browse products

`GET /api/products?q=java&categoryId=1&page=0&size=20&sort=createdAt`

Allowed sort fields: `name`, `price`, `createdAt`.

### Cart and checkout

- `GET /api/cart`
- `POST /api/cart/items/{productId}` with `{"quantity":2}`
- `PUT /api/cart/items/{productId}` with `{"quantity":1}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/orders/checkout` with `{"idempotencyKey":"checkout-unique-001"}`
- `GET /api/orders`
- `GET /api/orders/{id}`

### Stripe webhooks

Configure a local Stripe CLI forwarding target:

```bash
stripe listen --forward-to localhost:8080/api/webhooks/stripe
```

Copy the generated webhook signing secret to `STRIPE_WEBHOOK_SECRET`. Test the `checkout.session.completed` event using Stripe test mode.

## Security and engineering notes

- Never store raw passwords; this project uses BCrypt.
- Refresh tokens are signed and time-limited, but this baseline does not persist refresh-token families or support revocation. For production, persist hashed refresh tokens, rotate them on refresh, and revoke on logout.
- Add rate limiting for login/registration, email verification, password reset, CORS allow-list configuration, security headers, and audit events before public deployment.
- Payment events must be idempotent. This baseline ignores duplicate `PAID` transitions but should be hardened with Stripe event-ID persistence and transactional stock reservation before real use.
- Inventory needs pessimistic locking or atomic conditional stock updates to eliminate oversell races under concurrency. Do not deploy this starter for real transactions before implementing and testing this.
- Product cache entries are invalidated on product writes. If multiple instances are used, use shared Redis and consider cache stampede protection.
- Avoid exposing Actuator metrics publicly in production.
- Use a managed secrets store in deployment; never commit API keys.

## Testing checklist

- `mvn clean test`
- Verify register/login and invalid credentials.
- Verify `USER` cannot create/update/delete products.
- Verify public product browse and pagination.
- Verify cart quantity validation and stock errors.
- Verify duplicate checkout keys and Stripe webhook signature validation.
- Use Testcontainers for database-backed integration tests when Docker is available.

## Resume description (only after verifying and measuring)

**E-commerce Backend | Java, Spring Boot, MySQL, Redis, Spring Security, JWT, Stripe, Docker**
- Built REST APIs for catalog, authentication, cart, orders, and payment checkout using layered Spring Boot architecture.
- Implemented JWT-based stateless authentication, BCrypt password hashing, and role-based access control.
- Added Redis caching, pagination, database indexes, Flyway migrations, and Docker Compose for reproducible local environments.
- Added automated tests and Stripe webhook handling; benchmarked catalog endpoints before/after caching.

Replace generic statements with actual test coverage and measured latency results after you run the project.
