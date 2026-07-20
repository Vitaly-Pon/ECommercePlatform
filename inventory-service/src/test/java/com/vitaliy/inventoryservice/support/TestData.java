package com.vitaliy.inventoryservice.support;

import com.vitaliy.inventoryservice.dto.ReserveRequest;

public class TestData {

    public static final String PRODUCT_ID = "product-1";
    public static final String RESERVATION_ID = "reservation-1";
    public static final int QUANTITY = 2;

    public static final String RACE_PRODUCT_ID = "product-race";
    public static final String RACE_RESERVATION_ID = "race-reservation";

    public static ReserveRequest reserveRequest() {
        ReserveRequest request = new ReserveRequest();
        request.setProductId(PRODUCT_ID);
        request.setQuantity(QUANTITY);
        request.setReservationId(RESERVATION_ID);
        return request;
    }

    public static ReserveRequest raceReserveRequest() {
        ReserveRequest request = new ReserveRequest();
        request.setProductId(RACE_PRODUCT_ID);
        request.setQuantity(QUANTITY);
        request.setReservationId(RACE_RESERVATION_ID);
        return request;
    }

    private TestData() {
    }
}