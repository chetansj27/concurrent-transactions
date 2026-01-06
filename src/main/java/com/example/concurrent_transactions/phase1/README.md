# Phase 1: Basic Seat Booking (No Locking)

## What This Phase Does

Phase 1 implements a basic seat booking system **without any concurrency control mechanisms**. It demonstrates what happens when multiple threads try to book the same seat simultaneously.

### Features
- Create seats
- Book seats
- Cancel bookings
- Track booking history

## The Problem: Race Condition

When multiple concurrent requests try to book the same seat, **multiple bookings succeed** when only **one should succeed**.

### What Happens

1. **Thread 1** reads seat → `isAvailable = true` ✓
2. **Thread 2** reads seat → `isAvailable = true` ✓ (reads before Thread 1 saves)
3. **Thread 3** reads seat → `isAvailable = true` ✓ (reads before Thread 1 saves)
4. All threads proceed to book → **Multiple bookings succeed** ❌

### Root Cause

The `BookingService.processBooking()` method uses:
```java
Seat seat = seatRepository.findById(seatId);  // No locking!
```

This is the classic **check-then-act** pattern without synchronization:
- **Check**: Read if seat is available
- **Act**: Update seat to booked

Between the check and act, other threads can also read the seat as available.

### Test Results

Running `ConcurrentTestRunner` with 20 concurrent requests for the same seat:
- **Expected**: Only 1 booking should succeed
- **Actual**: 7+ bookings succeed (race condition)

## Why This Matters

This demonstrates the fundamental problem in concurrent systems:
- **Lost Updates**: Multiple transactions overwrite each other
- **Data Inconsistency**: Seat appears booked multiple times
- **Business Logic Violation**: One seat, multiple bookings

## Next Steps

In later phases, we'll fix this using:
- **Phase 2**: Pessimistic Locking
- **Phase 3**: Optimistic Locking  
- **Phase 4+**: Other concurrency control mechanisms

