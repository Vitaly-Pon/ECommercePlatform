package com.vitaliy.inventoryservice.service;

import com.vitaliy.inventoryservice.domain.Inventory;
import com.vitaliy.inventoryservice.domain.Reservation;
import com.vitaliy.inventoryservice.dto.InventoryResponse;
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import com.vitaliy.inventoryservice.repository.ReservationRepository;
import com.vitaliy.inventoryservice.support.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void shouldReserveProductSuccessfully() {

        Inventory inventory =
                Inventory.builder()
                        .productId(TestData.PRODUCT_ID)
                        .quantity(10)
                        .reserved(0)
                        .build();


        when(reservationRepository
                .findByReservationId(
                        TestData.RESERVATION_ID
                ))
                .thenReturn(Optional.empty());


        when(inventoryRepository
                .reserveStock(
                        TestData.PRODUCT_ID,
                        2
                ))
                .thenReturn(1);


        when(inventoryRepository
                .findByProductId(
                        TestData.PRODUCT_ID
                ))
                .thenReturn(Optional.of(inventory));



        InventoryResponse response =
                inventoryService.reserve(
                        TestData.reserveRequest()
                );


        assertEquals(
                10,
                response.getQuantity()
        );


        verify(reservationRepository)
                .save(any(Reservation.class));

        verify(inventoryRepository)
                .reserveStock(
                        TestData.PRODUCT_ID,
                        TestData.QUANTITY
                );
    }

    @Test
    void shouldNotReserveTwiceWithSameReservationId() {

        Reservation reservation =
                Reservation.builder()
                        .reservationId(
                                TestData.RESERVATION_ID
                        )
                        .productId(
                                TestData.PRODUCT_ID
                        )
                        .quantity(2)
                        .build();


        when(reservationRepository
                .findByReservationId(
                        TestData.RESERVATION_ID
                ))
                .thenReturn(Optional.of(reservation));


        Inventory inventory =
                Inventory.builder()
                        .productId(TestData.PRODUCT_ID)
                        .quantity(10)
                        .reserved(2)
                        .build();


        when(inventoryRepository
                .findByProductId(
                        TestData.PRODUCT_ID
                ))
                .thenReturn(Optional.of(inventory));



        InventoryResponse response =
                inventoryService.reserve(
                        TestData.reserveRequest()
                );


        verify(inventoryRepository, never())
                .reserveStock(anyString(), anyInt());

        verify(reservationRepository, never())
                .save(any(Reservation.class));


        assertEquals(
                2,
                response.getReserved()
        );
    }
}
