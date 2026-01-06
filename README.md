# Concurrent Transactions Project

A learning project exploring concurrent transaction handling in distributed systems.

## Phase 1: Seat Booking System

Seat booking system for testing concurrent read/write operations. Built with Spring Boot, JPA, and PostgreSQL.

### API Endpoints

- `POST /api/phase1/setup?seats=10` - Create test seats (run once before testing)
- `POST /api/phase1/book` - Book a seat (for concurrent testing)
  ```json
  {"seatId": 1, "customerName": "John"}
  ```
- `POST /api/phase1/cancel` - Cancel booking (for concurrent testing)
  ```json
  {"seatId": 1, "customerName": "John"}
  ```

### Setup

1. Create database: `CREATE DATABASE concurrent_transactions;`
2. Run the application - Flyway will apply migrations automatically

### Fix Migration Checksum Error

If you get a checksum mismatch error, reset the database:
```sql
DROP DATABASE IF EXISTS concurrent_transactions;
CREATE DATABASE concurrent_transactions;
```