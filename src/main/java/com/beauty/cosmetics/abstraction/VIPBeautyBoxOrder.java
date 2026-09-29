package com.beauty.cosmetics.abstraction;

import com.beauty.cosmetics.implementor.CosmeticSupplier;
import com.beauty.cosmetics.model.CosmeticCategory;
import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import java.util.ArrayList;
import java.util.List;

public class VIPBeautyBoxOrder extends CosmeticOrder {

    public VIPBeautyBoxOrder(CosmeticSupplier supplier) {
        super(supplier);
    }

    @Override
    public OrderFulfillmentResult checkout(String customerId, List<String> items, double totalAmount, CosmeticCategory category) {
        // VIP: авто-добавление бесплатного сэмпла и праздничной упаковки
        List<String> vipItems = new ArrayList<>(items);
        vipItems.add("SAMPLE-LUXURY-SERUM-5ML");
        vipItems.add("GIFT-BOX-PACKAGING");

        OrderPayload payload = new OrderPayload(customerId, vipItems, totalAmount, category);

        // Повторная попытка при сбое
        OrderFulfillmentResult result = supplier.processOrder(payload);
        if (!result.success()) {
            result = supplier.processOrder(payload);
        }
        return result;
    }
}
