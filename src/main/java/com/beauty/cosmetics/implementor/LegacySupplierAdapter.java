package com.beauty.cosmetics.implementor;

import com.beauty.cosmetics.legacy.LegacyB2BConnectionException;
import com.beauty.cosmetics.legacy.LegacyBrandB2BApi;
import com.beauty.cosmetics.model.CosmeticCategory;
import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import java.util.UUID;

public class LegacySupplierAdapter implements CosmeticSupplier {
    private final LegacyBrandB2BApi legacyApi;

    public LegacySupplierAdapter(LegacyBrandB2BApi legacyApi) {
        this.legacyApi = legacyApi;
    }

    @Override
    public OrderFulfillmentResult processOrder(OrderPayload payload) {
        String[] itemCodes = payload.items().toArray(new String[0]);
        int amountInCents = (int) Math.round(payload.totalAmount() * 100);
        boolean isPriority = payload.category() == CosmeticCategory.PERFUME;

        try {
            int statusCode = legacyApi.executeBatchSupplyRequest(
                    itemCodes,
                    amountInCents,
                    payload.customerId(),
                    isPriority
            );

            if (statusCode == 200) {
                return OrderFulfillmentResult.ok("LEGACY-B2B-" + UUID.randomUUID().toString().substring(0, 8));
            } else {
                return OrderFulfillmentResult.fail("B2B Supplier Error Code: " + statusCode);
            }
        } catch (LegacyB2BConnectionException e) {
            return OrderFulfillmentResult.fail("B2B Network Failure: " + e.getMessage());
        } catch (Exception e) {
            return OrderFulfillmentResult.fail("Unexpected Adapter Exception: " + e.getMessage());
        }
    }
}
