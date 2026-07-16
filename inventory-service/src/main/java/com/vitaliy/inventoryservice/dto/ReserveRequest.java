package com.vitaliy.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReserveRequest {
    @NotBlank
    private String productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotBlank
    private String reservationId;
}