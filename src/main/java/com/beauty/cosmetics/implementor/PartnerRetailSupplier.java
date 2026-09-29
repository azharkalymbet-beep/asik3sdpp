package com.beauty.cosmetics.implementor;

import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import java.util.UUID;

public class PartnerRetailSupplier implements CosmeticSupplier {
    @Override
    public OrderFulfillmentResult processOrder(OrderPayload payload) {
        return OrderFulfillmentResult.ok("PRT-RETAIL-" + UUID.randomUUID().toString().substring(0, 8));
    }
}
