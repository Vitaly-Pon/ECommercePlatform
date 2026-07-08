package com.vitaliy.orderservice.dto;

import lombok.Data;

@Data
public class InventoryResponse {
    private Long id;
    private String productId;
    private Integer quantity;
    private Integer reserved;
    private Integer available;
}
