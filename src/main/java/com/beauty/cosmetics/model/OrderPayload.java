package com.beauty.cosmetics.model;

import java.util.List;

public record OrderPayload(
        String customerId,
        List<String> items,
        double totalAmount,
        CosmeticCategory category
) {}
