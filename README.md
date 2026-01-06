# Concurrent Transactions Project

A learning project exploring concurrent transaction handling in distributed systems.

## Phase 1: Seat Booking System

Seat booking system for testing concurrent read/write operations. Built with Spring Boot, JPA, and PostgreSQL.

### Quick Test

Run `phase1.ConcurrentTestRunner.main()` - it will:
1. Create 10 test seats
2. Make 20 concurrent booking requests for the same seat
3. Show results (only 1 should succeed)

### API Endpoints

- `POST /api/phase1/setup?seats=10` - Create test seats
- `POST /api/phase1/book` - Book a seat
  ```json
  {"seatId": 1, "customerName": "John"}
  ```
- `POST /api/phase1/cancel` - Cancel booking
  ```json
  {"seatId": 1, "customerName": "John"}
  ```