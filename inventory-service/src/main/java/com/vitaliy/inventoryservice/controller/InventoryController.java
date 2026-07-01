package com.vitaliy.inventoryservice.controller;

import com.vitaliy.inventoryservice.dto.InventoryResponse;
import com.vitaliy.inventoryservice.dto.ReserveRequest;
import com.vitaliy.inventoryservice.dto.StockRequest;
import com.vitaliy.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/stock")
    public InventoryResponse addStock(@Valid @RequestBody StockRequest request) {
        return service.addStock(request);
    }

    @PostMapping("/reserve")
    public InventoryResponse reserve(@Valid @RequestBody ReserveRequest request) {
        return service.reserve(request);
    }

    @PostMapping("/release")
    public InventoryResponse release(@Valid @RequestBody ReserveRequest request) {
        return service.release(request);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getByProductId(@PathVariable String productId) {
        return service.getByProductId(productId);
    }

    @GetMapping
    public List<InventoryResponse> getAll() {
        return service.getAll();
    }
}
