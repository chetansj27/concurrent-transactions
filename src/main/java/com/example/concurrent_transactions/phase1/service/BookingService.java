package com.example.concurrent_transactions.phase1.service;

import com.example.concurrent_transactions.phase1.dto.BookingRequest;
import com.example.concurrent_transactions.phase1.dto.BookingResponse;
import com.example.concurrent_transactions.phase1.entity.Booking;
import com.example.concurrent_transactions.phase1.entity.Seat;
import com.example.concurrent_transactions.phase1.repository.BookingRepository;
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
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public BookingResponse bookSeat(BookingRequest request) {
        log.info("Processing booking: seatId={}, customerName={}", request.seatId(), request.customerName());
        
        if (request.bookingStatus() != Booking.BookingStatus.BOOKED) {
            throw new IllegalArgumentException("Only BOOKED status is allowed for booking operation");
        }
        
        return processBooking(request.seatId(), request.bookingStatus(), request.customerName());
    }

    public BookingResponse cancelBooking(BookingRequest request) {
        log.info("Processing cancellation: seatId={}, customerName={}", request.seatId(), request.customerName());
        
        if (request.bookingStatus() != Booking.BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Only CANCELLED status is allowed for cancellation operation");
        }
        
        return processBooking(request.seatId(), request.bookingStatus(), request.customerName());
    }

    private BookingResponse processBooking(Long seatId, Booking.BookingStatus status, String customerName) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + seatId));

        Boolean statusBefore = seat.getIsAvailable();
        Boolean statusAfter;

        if (status == Booking.BookingStatus.BOOKED) {
            if (!statusBefore) {
                throw new IllegalArgumentException("Seat is already booked");
            }
            statusAfter = false;
            seat.setIsAvailable(false);
            seat.setCustomerName(customerName);
        } else { // CANCELLED
            if (statusBefore) {
                throw new IllegalArgumentException("Seat is already available");
            }
            statusAfter = true;
            seat.setIsAvailable(true);
            seat.setCustomerName(null);
        }

        seatRepository.save(seat);

        Booking booking = Booking.builder()
                .seat(seat)
                .bookingStatus(status)
                .customerName(customerName)
                .statusBefore(statusBefore)
                .statusAfter(statusAfter)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking completed: bookingId={}, statusBefore={}, statusAfter={}", 
                savedBooking.getId(), statusBefore, statusAfter);

        return mapToResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsBySeatId(Long seatId) {
        log.debug("Fetching bookings for seat ID: {}", seatId);
        return bookingRepository.findBySeatIdOrderByCreatedAtDesc(seatId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        log.debug("Fetching booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getSeat().getId(),
                booking.getBookingStatus(),
                booking.getCustomerName(),
                booking.getStatusBefore(),
                booking.getStatusAfter(),
                booking.getCreatedAt()
        );
    }
}

