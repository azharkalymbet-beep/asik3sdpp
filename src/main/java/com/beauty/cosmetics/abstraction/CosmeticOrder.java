package com.beauty.cosmetics.abstraction;

import com.beauty.cosmetics.implementor.CosmeticSupplier;
import com.beauty.cosmetics.model.CosmeticCategory;
import com.beauty.cosmetics.model.OrderFulfillmentResult;
import java.util.List;

public abstract class CosmeticOrder {
    protected final CosmeticSupplier supplier;

    protected CosmeticOrder(CosmeticSupplier supplier) {
        this.supplier = supplier;
    }

    public abstract OrderFulfillmentResult checkout(String customerId, List<String> items, double totalAmount, CosmeticCategory category);
}
