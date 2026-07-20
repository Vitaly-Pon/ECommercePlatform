package com.vitaliy.inventoryservice.service;


import com.vitaliy.inventoryservice.domain.Inventory;
import com.vitaliy.inventoryservice.domain.Reservation;
import com.vitaliy.inventoryservice.dto.InventoryResponse;
import com.vitaliy.inventoryservice.dto.ReserveRequest;
import com.vitaliy.inventoryservice.dto.StockRequest;
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import com.vitaliy.inventoryservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public InventoryResponse addStock(StockRequest request) {

        Inventory inventory = inventoryRepository
                .findByProductId(request.getProductId())
                .orElseGet(() -> Inventory.builder()
                        .productId(request.getProductId())
                        .quantity(0)
                        .reserved(0)
                        .build());

        inventory.setQuantity(
                inventory.getQuantity() + request.getQuantity()
        );

        return toResponse(
                inventoryRepository.save(inventory)
        );
    }

    @Transactional
    public InventoryResponse release(ReserveRequest request) {

        Reservation reservation =
                reservationRepository
                        .findByReservationId(request.getReservationId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Reservation not found")
                        );
        if (reservation.isReleased()) {
            return getInventoryResponse(
                    reservation.getProductId()
            );
        }

        Inventory inventory =
                inventoryRepository.findByProductId(
                                reservation.getProductId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found")
                        );
        inventory.release(
                reservation.getQuantity()
        );
        reservation.setReleased(true);
        reservationRepository.save(reservation);

        return toResponse(inventory);
    }

    @Transactional
    public InventoryResponse reserve(ReserveRequest request) {

        Optional<Reservation> existing =
                reservationRepository.findByReservationId(
                        request.getReservationId()
                );

        if (existing.isPresent()) {
            return getInventoryResponse(
                    existing.get().getProductId()
            );
        }

        int updated =
                inventoryRepository.reserveStock(
                        request.getProductId(),
                        request.getQuantity()
                );


        if (updated == 0) {
            throw new RuntimeException("Not enough stock");
        }

        reservationRepository.save(
                Reservation.builder()
                        .reservationId(request.getReservationId())
                        .productId(request.getProductId())
                        .quantity(request.getQuantity())
                        .createdAt(Instant.now())
                        .build()
        );

        return getInventoryResponse(
                request.getProductId()
        );
    }

    public InventoryResponse getByProductId(String productId) {

        Inventory inventory =
                inventoryRepository.findByProductId(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );


        return toResponse(inventory);
    }


    public List<InventoryResponse> getAll() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private InventoryResponse getInventoryResponse(String productId) {

        Inventory inventory =
                inventoryRepository.findByProductId(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );


        return toResponse(inventory);
    }


    private InventoryResponse toResponse(Inventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .reserved(inventory.getReserved())
                .available(inventory.getAvailable())
                .build();
    }
}