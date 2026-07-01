package com.vitaliy.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InventoryResponse {
    private Long id;
    private String productId;
    private Integer quantity;
    private Integer reserved;
    private Integer available;
}
