package com.vitaliy.orderservice.grpc;

import inventory.Inventory;
import inventory.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcInventoryClient {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    public GrpcInventoryClient(
            @Value("${grpc.client.inventory.host}") String host,
            @Value("${grpc.client.inventory.port}") int port) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = InventoryServiceGrpc.newBlockingStub(channel);
    }

    public boolean checkStock(String productId, int quantity) {
        Inventory.StockRequest request = Inventory.StockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        return stub.checkStock(request).getAvailable();
    }

    public void reserveStock(String productId, int quantity) {
        Inventory.ReserveRequest request = Inventory.ReserveRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        stub.reserveStock(request);
    }
}