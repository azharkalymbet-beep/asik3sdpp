package com.beauty.cosmetics.abstraction;

import com.beauty.cosmetics.implementor.CosmeticSupplier;
import com.beauty.cosmetics.model.CosmeticCategory;
import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import java.util.List;

public class StandardCosmeticOrder extends CosmeticOrder {

    public StandardCosmeticOrder(CosmeticSupplier supplier) {
        super(supplier);
    }

    @Override
    public OrderFulfillmentResult checkout(String customerId, List<String> items, double totalAmount, CosmeticCategory category) {
        OrderPayload payload = new OrderPayload(customerId, items, totalAmount, category);
        return supplier.processOrder(payload);
    }
}
