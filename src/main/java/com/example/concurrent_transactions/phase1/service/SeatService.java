package com.example.concurrent_transactions.phase1.service;

import com.example.concurrent_transactions.phase1.dto.SeatRequest;
import com.example.concurrent_transactions.phase1.dto.SeatResponse;
import com.example.concurrent_transactions.phase1.entity.Seat;
import com.example.concurrent_transactions.phase1.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatResponse createSeat(SeatRequest request) {
        log.info("Creating seat with number: {}", request.seatNumber());
        
        if (seatRepository.findBySeatNumber(request.seatNumber()).isPresent()) {
            throw new IllegalArgumentException("Seat with number " + request.seatNumber() + " already exists");
        }

        Seat seat = Seat.builder()
                .seatNumber(request.seatNumber())
                .isAvailable(true)
                .build();

        Seat savedSeat = seatRepository.save(seat);
        log.info("Seat created with ID: {}", savedSeat.getId());
        
        return mapToResponse(savedSeat);
    }

    @Transactional(readOnly = true)
    public SeatResponse getSeatById(Long id) {
        log.debug("Fetching seat with ID: {}", id);
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + id));
        return mapToResponse(seat);
    }

    @Transactional(readOnly = true)
    public SeatResponse getSeatByNumber(String seatNumber) {
        log.debug("Fetching seat with number: {}", seatNumber);
        Seat seat = seatRepository.findBySeatNumber(seatNumber)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with number: " + seatNumber));
        return mapToResponse(seat);
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> getAllSeats() {
        log.debug("Fetching all seats");
        return seatRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SeatResponse mapToResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getIsAvailable(),
                seat.getCustomerName(),
                seat.getCreatedAt(),
                seat.getUpdatedAt()
        );
    }
}

