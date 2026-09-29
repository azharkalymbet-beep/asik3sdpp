package com.beauty.cosmetics.implementor;

import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;

public interface CosmeticSupplier {
    OrderFulfillmentResult processOrder(OrderPayload payload);
}
