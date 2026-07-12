package com.vitaliy.inventoryservice.repository;

import com.vitaliy.inventoryservice.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(String productId);

    @Modifying
    @Query("UPDATE Inventory i SET i.reserved = i.reserved + :amount WHERE i.productId = :productId AND (i.quantity - i.reserved) >= :amount")
    int reserveStock(@Param("productId") String productId, @Param("amount") int amount);
}
