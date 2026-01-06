package com.example.concurrent_transactions.phase1.repository;

import com.example.concurrent_transactions.phase1.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBySeatIdOrderByCreatedAtDesc(Long seatId);

    Page<Booking> findBySeatIdOrderByCreatedAtDesc(Long seatId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.seat.id = :seatId ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookingsBySeatId(@Param("seatId") Long seatId, Pageable pageable);
}

