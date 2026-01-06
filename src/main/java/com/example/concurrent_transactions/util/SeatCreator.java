package com.example.concurrent_transactions.util;

import com.example.concurrent_transactions.phase1.dto.SeatRequest;
import com.example.concurrent_transactions.phase1.dto.SeatResponse;
import com.example.concurrent_transactions.phase1.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class to create test seats.
 * 
 * Usage:
 * Inject SeatCreator and call:
 * - createSeat(seatNumber) - Create single seat
 * - createTestSeats(count) - Create multiple test seats
 * - createRandomSeat() - Create seat with random number
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SeatCreator {

    private final SeatService seatService;

    /**
     * Creates a single test seat with specified parameters.
     */
    public SeatResponse createSeat(String seatNumber) {
        log.info("Creating seat: {}", seatNumber);
        
        SeatRequest request = new SeatRequest(seatNumber);

        try {
            SeatResponse response = seatService.createSeat(request);
            log.info("Seat created successfully - ID: {}, Number: {}, Available: {}", 
                    response.id(), response.seatNumber(), response.isAvailable());
            return response;
        } catch (Exception e) {
            log.error("Failed to create seat: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Creates multiple test seats with default naming.
     */
    public List<SeatResponse> createTestSeats(int count) {
        log.info("Creating {} test seats", count);
        
        List<SeatResponse> createdSeats = new ArrayList<>();
        
        for (int i = 1; i <= count; i++) {
            String seatNumber = "SEAT-" + i;
            
            try {
                SeatResponse seat = createSeat(seatNumber);
                createdSeats.add(seat);
                log.info("[{}/{}] Created seat: {} (ID: {})", i, count, seatNumber, seat.id());
            } catch (Exception e) {
                log.error("[{}/{}] Failed to create seat: {} - {}", i, count, seatNumber, e.getMessage());
            }
        }
        
        log.info("Successfully created {}/{} seats", createdSeats.size(), count);
        return createdSeats;
    }

    /**
     * Creates a test seat with random seat number.
     */
    public SeatResponse createRandomSeat() {
        Random random = new Random();
        String seatNumber = "SEAT-" + (1000 + random.nextInt(9000));
        return createSeat(seatNumber);
    }
}

