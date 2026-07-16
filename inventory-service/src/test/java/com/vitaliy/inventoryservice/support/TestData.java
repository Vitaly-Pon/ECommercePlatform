package com.vitaliy.inventoryservice.support;

import com.vitaliy.inventoryservice.dto.ReserveRequest;

public class TestData {
    public static final String PRODUCT_ID = "product-1";

    public static final String RESERVATION_ID =
            "reservation-1";

    public static final int QUANTITY = 2;

    public static ReserveRequest reserveRequest() {

        ReserveRequest request = new ReserveRequest();

        request.setProductId(PRODUCT_ID);
        request.setQuantity(QUANTITY);
        request.setReservationId(RESERVATION_ID);

        return request;
    }


    private TestData() {
    }
}