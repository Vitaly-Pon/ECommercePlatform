package com.vitaliy.inventoryservice.grpc;

import com.vitaliy.inventoryservice.domain.Inventory;  // ← твой доменный класс
import com.vitaliy.inventoryservice.repository.InventoryRepository;
import inventory.Inventory.StockRequest;
import inventory.Inventory.StockResponse;
import inventory.Inventory.ReserveRequest;
import inventory.Inventory.ReserveResponse;            // ← сгенерированные proto-классы
import inventory.InventoryServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryRepository repository;

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
            Inventory inventory = repository.findByProductId(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            inventory.reserve(request.getQuantity());
            repository.save(inventory);

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
