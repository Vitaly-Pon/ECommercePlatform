package com.vitaliy.inventoryservice.grpc;

import com.vitaliy.inventoryservice.domain.Inventory;  // ← твой доменный класс
import com.vitaliy.inventoryservice.dto.InventoryResponse;
import com.vitaliy.inventoryservice.event.InventoryEvent;
import com.vitaliy.inventoryservice.kafka.InventoryProducer;
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import com.vitaliy.inventoryservice.service.InventoryService;
import inventory.Inventory.StockRequest;
import inventory.Inventory.StockResponse;
import inventory.Inventory.ReserveRequest;
import inventory.Inventory.ReserveResponse;            // ← сгенерированные proto-классы
import inventory.InventoryServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryRepository repository;
    private final InventoryProducer inventoryProducer;
    private final InventoryService inventoryService;

    @Override
    public void checkStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        Inventory inventory = repository.findByProductId(request.getProductId()).orElse(null);

        StockResponse response = StockResponse.newBuilder()
                .setAvailable(inventory != null && inventory.getAvailable() >= request.getQuantity())
                .setAvailableQuantity(inventory != null ? inventory.getAvailable() : 0)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void reserveStock(ReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {
        try {
            com.vitaliy.inventoryservice.dto.ReserveRequest dtoRequest =
                    new com.vitaliy.inventoryservice.dto.ReserveRequest();
            dtoRequest.setProductId(request.getProductId());
            dtoRequest.setQuantity((int) request.getQuantity());

            InventoryResponse response = inventoryService.reserve(dtoRequest);

            inventoryProducer.sendReserved(InventoryEvent.newBuilder()
                    .setReservationId(request.getReservationId())
                    .setProductId(request.getProductId())
                    .setQuantity(request.getQuantity())
                    .setOrderId(request.getOrderId())
                    .setStatus("RESERVED")
                    .setTimestamp(Instant.now().toString())
                    .build());

            responseObserver.onNext(ReserveResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Reserved " + request.getQuantity())
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(ReserveResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }
}
