package com.beauty.cosmetics.implementor;

import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import java.util.UUID;

public class InternalWarehouseSupplier implements CosmeticSupplier {
    @Override
    public OrderFulfillmentResult processOrder(OrderPayload payload) {
        return OrderFulfillmentResult.ok("INT-WH-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
