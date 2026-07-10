package com.vitaliy.orderservice.grpc;

import inventory.Inventory;
import inventory.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcInventoryClient {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    public GrpcInventoryClient() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stub = InventoryServiceGrpc.newBlockingStub(channel);
    }

    public boolean checkStock(String productId, int quantity) {
        Inventory.StockRequest request = Inventory.StockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        Inventory.StockResponse response = stub.checkStock(request);
        return response.getAvailable();
    }

    public void reserveStock(String productId, int quantity) {
        Inventory.ReserveRequest request = Inventory.ReserveRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        stub.reserveStock(request);
    }
}