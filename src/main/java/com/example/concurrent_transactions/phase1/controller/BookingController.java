package com.example.concurrent_transactions.phase1.controller;

import com.example.concurrent_transactions.phase1.dto.BookingRequest;
import com.example.concurrent_transactions.phase1.dto.BookingResponse;
import com.example.concurrent_transactions.phase1.dto.SimpleBookingRequest;
import com.example.concurrent_transactions.phase1.entity.Booking;
import com.example.concurrent_transactions.phase1.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phase1")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    /**
     * Book a seat - for concurrent testing
     * POST /api/phase1/book
     * Body: {"seatId": 1, "customerName": "John"}
     */
    @PostMapping("/book")
    public ResponseEntity<BookingResponse> book(@RequestBody SimpleBookingRequest request) {
        BookingResponse response = bookingService.bookSeat(
                new BookingRequest(request.seatId(), Booking.BookingStatus.BOOKED, request.customerName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cancel a booking - for concurrent testing
     * POST /api/phase1/cancel
     * Body: {"seatId": 1, "customerName": "John"}
     */
    @PostMapping("/cancel")
    public ResponseEntity<BookingResponse> cancel(@RequestBody SimpleBookingRequest request) {
        BookingResponse response = bookingService.cancelBooking(
                new BookingRequest(request.seatId(), Booking.BookingStatus.CANCELLED, request.customerName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

