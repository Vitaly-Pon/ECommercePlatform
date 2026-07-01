package com.vitaliy.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotBlank
    private String userId;

    @NotEmpty
    private List<OrderItemRequest> items;
}
