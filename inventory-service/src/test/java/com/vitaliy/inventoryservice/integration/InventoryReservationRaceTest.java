package com.vitaliy.inventoryservice.integration;

import com.vitaliy.inventoryservice.domain.Inventory;
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import com.vitaliy.inventoryservice.repository.ReservationRepository;
import com.vitaliy.inventoryservice.service.InventoryService;
import com.vitaliy.inventoryservice.support.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InventoryReservationRaceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        inventoryRepository.deleteAll();

        inventoryRepository.save(
                Inventory.builder()
                        .productId(TestData.RACE_PRODUCT_ID)
                        .quantity(10)
                        .reserved(0)
                        .build()
        );
    }

    @Test
    void shouldReserveOnceOnConcurrentRequests()
            throws Exception {

        int threads = 10;

        ExecutorService executor =
                Executors.newFixedThreadPool(threads);

        CountDownLatch latch = new CountDownLatch(1);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(
                    executor.submit(() -> {
                        latch.await();

                        try {
                            inventoryService.reserve(
                                    TestData.raceReserveRequest()
                            );
                        } catch (Exception ignored) {
                        }

                        return null;
                    })
            );
        }
        latch.countDown();

        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();

        Inventory inventory =
                inventoryRepository.findByProductId(TestData.RACE_PRODUCT_ID)
                        .orElseThrow();

        assertEquals(2, inventory.getReserved());
        assertEquals(1, reservationRepository.count());
    }
}