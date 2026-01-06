package com.example.concurrent_transactions.phase1.controller;

import com.example.concurrent_transactions.util.SeatCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phase1")
@RequiredArgsConstructor
@Slf4j
public class SeatController {

    private final SeatCreator seatCreator;

    /**
     * Setup test data - creates N seats for concurrent testing
     * POST /api/phase1/setup?seats=10
     */
    @PostMapping("/setup")
    public ResponseEntity<String> setup(@RequestParam(defaultValue = "10") int seats) {
        log.info("Setting up {} seats for testing", seats);
        seatCreator.createTestSeats(seats);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created " + seats + " seats");
    }
}

