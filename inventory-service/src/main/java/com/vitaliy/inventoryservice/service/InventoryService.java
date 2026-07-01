package com.vitaliy.inventoryservice.service;


import com.vitaliy.inventoryservice.domain.Inventory;
import com.vitaliy.inventoryservice.dto.InventoryResponse;
import com.vitaliy.inventoryservice.dto.ReserveRequest;
import com.vitaliy.inventoryservice.dto.StockRequest;
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional
    public InventoryResponse addStock(StockRequest request) {
        Inventory inventory = repository.findByProductId(request.getProductId())
                .orElseGet(() -> Inventory.builder()
                        .productId(request.getProductId())
                        .quantity(0)
                        .reserved(0)
                        .build());
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        return toResponse(repository.save(inventory));
    }

    @Transactional
    public InventoryResponse reserve(ReserveRequest request) {
        Inventory inventory = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found in inventory"));
        inventory.reserve(request.getQuantity());
        return toResponse(repository.save(inventory));
    }

    @Transactional
    public InventoryResponse release(ReserveRequest request) {
        Inventory inventory = repository.findByProductId(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found in inventory/Товар не найден на складе"));
        inventory.release(request.getQuantity());
        return toResponse(repository.save(inventory));
    }

    public InventoryResponse getByProductId(String productId) {
        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in inventory/Товар не найден на складе"));
        return toResponse(inventory);
    }

    public List<InventoryResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    private InventoryResponse toResponse(Inventory i) {
        return InventoryResponse.builder()
                .id(i.getId())
                .productId(i.getProductId())
                .quantity(i.getQuantity())
                .reserved(i.getReserved())
                .available(i.getAvailable())
                .build();
    }
}
