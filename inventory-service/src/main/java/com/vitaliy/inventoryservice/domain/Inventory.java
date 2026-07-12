package com.vitaliy.inventoryservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer reserved;

    @Version
    private Long version;

    public Integer getAvailable() {
        return quantity - reserved;
    }

    public boolean canReserve(int amount) {
        return getAvailable() >= amount;
    }

    public void reserve(int amount) {
        if (!canReserve(amount)) {
            throw new RuntimeException("Not enough stock/Недостаточно товара на складе");
        }
        this.reserved += amount;
    }

    public void release(int amount) {
        if (this.reserved < amount) {
            throw new RuntimeException("Cannot release more than reserved/Невозможно освободить больше, чем зарезервировано");
        }
        this.reserved -= amount;
    }
}
