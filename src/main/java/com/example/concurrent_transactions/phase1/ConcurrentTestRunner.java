package com.example.concurrent_transactions.phase1;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test runner for concurrent booking operations (Phase 1)
 * Usage: Run this main method to test concurrent read/write scenarios
 * Make sure the Spring Boot application is running on port 8080
 */
@Slf4j
public class ConcurrentTestRunner {

    private static final String BASE_URL = "http://localhost:8080/api/phase1";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws Exception {
        log.info("=== Starting Concurrent Booking Test ===");

        // Step 1: Setup - Create seats
        int seatCount = 10;
        log.info("Step 1: Creating {} seats...", seatCount);
        setupSeats(seatCount);
        Thread.sleep(1000); // Wait a bit for setup to complete

        // Step 2: Concurrent booking test
        int concurrentRequests = 20;
        Long targetSeatId = 1L; // All threads try to book the same seat
        log.info("Step 2: Making {} concurrent booking requests for seat {}", concurrentRequests, targetSeatId);
        
        List<CompletableFuture<String>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);

        for (int i = 0; i < concurrentRequests; i++) {
            final int threadNum = i;
            final Long seatId = targetSeatId;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return bookSeat(seatId, "Customer-" + threadNum);
                } catch (Exception e) {
                    return "ERROR: " + e.getMessage();
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // Collect results
        int successCount = 0;
        int errorCount = 0;
        for (CompletableFuture<String> future : futures) {
            String result = future.get();
            if (result.contains("SUCCESS")) {
                successCount++;
            } else {
                errorCount++;
            }
            log.info("Result: {}", result);
        }

        log.info("\n=== Test Results ===");
        log.info("Total requests: {}", concurrentRequests);
        log.info("Successful bookings: {}", successCount);
        log.info("Failed/Errors: {}", errorCount);
        log.info("Expected: Only 1 should succeed (seat can only be booked once)");

        executor.shutdown();
    }

    private static void setupSeats(int count) throws Exception {
        String url = BASE_URL + "/setup?seats=" + count;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Setup response: {} - {}", response.statusCode(), response.body());
    }

    private static String bookSeat(Long seatId, String customerName) throws Exception {
        String jsonBody = String.format("{\"seatId\": %d, \"customerName\": \"%s\"}", seatId, customerName);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/book"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return String.format("SUCCESS: %s booked seat %d", customerName, seatId);
        } else {
            return String.format("FAILED: %s - Status: %d, Body: %s", customerName, response.statusCode(), response.body());
        }
    }
}

