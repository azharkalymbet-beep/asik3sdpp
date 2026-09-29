package com.beauty.cosmetics.model;

public record OrderFulfillmentResult(boolean success, String trackingNumber, String errorMessage) {
    public static OrderFulfillmentResult ok(String trackingNumber) {
        return new OrderFulfillmentResult(true, trackingNumber, null);
    }

    public static OrderFulfillmentResult fail(String errorMessage) {
        return new OrderFulfillmentResult(false, null, errorMessage);
    }
}
