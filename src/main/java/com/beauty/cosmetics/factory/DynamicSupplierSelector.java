package com.beauty.cosmetics.factory;

import com.beauty.cosmetics.implementor.CosmeticSupplier;
import com.beauty.cosmetics.implementor.InternalWarehouseSupplier;
import com.beauty.cosmetics.implementor.LegacySupplierAdapter;
import com.beauty.cosmetics.implementor.PartnerRetailSupplier;
import com.beauty.cosmetics.legacy.LegacyBrandB2BApi;
import com.beauty.cosmetics.model.CosmeticCategory;

public class DynamicSupplierSelector {
    private final LegacyBrandB2BApi legacyApi;

    public DynamicSupplierSelector(LegacyBrandB2BApi legacyApi) {
        this.legacyApi = legacyApi;
    }

    public CosmeticSupplier selectSupplier(CosmeticCategory category, String customerId) {
        if (category == CosmeticCategory.ORGANIC || customerId.startsWith("B2B_")) {
            return new LegacySupplierAdapter(legacyApi);
        } else if (customerId.startsWith("PARTNER_")) {
            return new PartnerRetailSupplier();
        } else {
            return new InternalWarehouseSupplier();
        }
    }
}
