# E-commerce Order Processing System

![CI](https://github.com/yexur3/ecommerce-order-service/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

A backend service for processing e-commerce orders — built solo, with a focus on correct business logic rather than just CRUD: order state transitions, inventory consistency under concurrent access, and handling an unreliable external payment provider.

## Features

- **Order lifecycle management** — a strict state machine (`PENDING → PAID → SHIPPED → DELIVERED`, with `CANCELLED` as a side branch) that rejects invalid status transitions
- **Inventory management** — stock is checked and decremented atomically when an order is placed, and restored automatically if an order is cancelled
- **Concurrency safety** — optimistic locking (JPA `@Version`) prevents overselling when multiple orders compete for the same limited stock
- **Payment simulation** — an imitated external payment gateway with simulated latency and a realistic failure rate, wired up with automatic retry on transient failures
- **Global exception handling** — all business-rule violations (insufficient stock, invalid transitions, payment failures, stock conflicts) return clear, structured HTTP error responses instead of raw stack traces

## Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Data JPA
- **Database:** PostgreSQL
- **Resilience:** Spring Retry (automatic retry on payment failure)
- **Testing:** JUnit 5, Mockito, Testcontainers
- **CI/CD:** GitHub Actions
- **Infrastructure:** Docker Compose (local development)

## How It Works

1. `POST /api/products` — register a product with a price and stock quantity
2. `POST /api/orders` — place an order for one or more products. The service validates stock availability, decrements it, and calculates the total order amount — all within a single transaction, so a failure partway through leaves no partial state
3. `PUT /api/orders/{id}/status` — move an order through its lifecycle. Every transition is validated against the order's current state; an invalid transition (e.g. skipping straight from `PENDING` to `DELIVERED`) is rejected with a clear error
4. `PUT /api/orders/{id}/pay` — attempt to pay for an order. Payment is simulated through a gateway with a chance of transient failure; failed attempts are retried automatically before the order is marked `PAID`
5. Cancelling an order (`CANCELLED`) automatically returns the reserved stock to inventory

## Architecture Notes

- **State machine**: valid transitions are defined directly on the `Status` enum via a `canTransitionTo()` method, keeping the transition rules in one place rather than scattered across service logic.
- **Transactional consistency**: order creation and status changes that touch multiple entities (order, order items, product stock) are wrapped in `@Transactional` methods, so partial writes never occur if an exception is thrown midway.
- **Optimistic locking**: `Product` uses a JPA `@Version` field. If two requests try to buy the last unit of a product at the same time, the second write fails with an `ObjectOptimisticLockingFailureException`, which is translated into a clear `409 Conflict` response rather than allowing the stock count to go negative.
- **Resilience pattern**: the payment simulation is deliberately isolated in its own service (`PaymentGatewaySimulator`) so that Spring's `@Retryable` proxy can intercept the call — a method can't be retried by Spring AOP when it's called on `this` from within the same class.
- **Testing strategy**: pure logic (the state machine) is covered by plain unit tests; service-layer behavior is covered with Mockito-mocked dependencies; and full request flows (order creation, stock updates, cancellation) are covered by Testcontainers-based integration tests against a real, ephemeral PostgreSQL instance.

## Running Locally

```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
./mvnw spring-boot:run

# Run the test suite (unit + integration, requires Docker for Testcontainers)
./mvnw test
```

The API will be available at `http://localhost:8080`.

## API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get a product by ID |
| POST | `/api/orders` | Create an order |
| GET | `/api/orders/{id}` | Get an order (with items) by ID |
| PUT | `/api/orders/{id}/status` | Change an order's status |
| PUT | `/api/orders/{id}/pay` | Attempt payment for an order |

## Possible Future Improvements

- Move order-shipped notifications to a message queue (RabbitMQ) instead of a direct call
- Redis caching for the product catalog
- User accounts with per-user order history
