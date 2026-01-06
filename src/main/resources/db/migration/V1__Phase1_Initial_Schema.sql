-- Phase 1: Initial Schema
-- This migration creates the basic table structure for learning concurrent transactions

-- Seats table for testing concurrent read/write operations
CREATE TABLE seats (
    id BIGSERIAL PRIMARY KEY,
    seat_number VARCHAR(50) UNIQUE NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT true,
    customer_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster lookups
CREATE INDEX idx_seats_seat_number ON seats(seat_number);

-- Bookings table for tracking operations
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    seat_id BIGINT NOT NULL,
    booking_status VARCHAR(20) NOT NULL, -- 'BOOKED', 'CANCELLED'
    customer_name VARCHAR(255) NOT NULL,
    status_before BOOLEAN NOT NULL,
    status_after BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- Index for seat bookings lookup
CREATE INDEX idx_bookings_seat_id ON bookings(seat_id);
CREATE INDEX idx_bookings_created_at ON bookings(created_at);

