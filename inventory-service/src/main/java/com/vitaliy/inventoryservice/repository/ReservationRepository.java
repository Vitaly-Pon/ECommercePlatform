package com.vitaliy.inventoryservice.repository;

import com.vitaliy.inventoryservice.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationId(String reservationId);
}
