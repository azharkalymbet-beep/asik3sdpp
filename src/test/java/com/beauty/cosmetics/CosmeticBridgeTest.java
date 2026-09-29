package com.beauty.cosmetics;

import com.beauty.cosmetics.abstraction.StandardCosmeticOrder;
import com.beauty.cosmetics.abstraction.VIPBeautyBoxOrder;
import com.beauty.cosmetics.factory.DynamicSupplierSelector;
import com.beauty.cosmetics.implementor.CosmeticSupplier;
import com.beauty.cosmetics.implementor.InternalWarehouseSupplier;
import com.beauty.cosmetics.implementor.LegacySupplierAdapter;
import com.beauty.cosmetics.implementor.PartnerRetailSupplier;
import com.beauty.cosmetics.legacy.LegacyB2BConnectionException;
import com.beauty.cosmetics.legacy.LegacyBrandB2BApi;
import com.beauty.cosmetics.model.CosmeticCategory;
import com.beauty.cosmetics.model.OrderFulfillmentResult;
import com.beauty.cosmetics.model.OrderPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CosmeticBridgeTest {

    @Mock
    private CosmeticSupplier mockSupplier;

    @Mock
    private LegacyBrandB2BApi mockLegacyApi;

    @Test
    void testStandardOrderDelegation() {
        when(mockSupplier.processOrder(any())).thenReturn(OrderFulfillmentResult.ok("TRACK-100"));

        StandardCosmeticOrder order = new StandardCosmeticOrder(mockSupplier);
        OrderFulfillmentResult result = order.checkout("CUST-1", List.of("LIPSTICK-RED"), 29.99, CosmeticCategory.MAKEUP);

        assertTrue(result.success());
        assertEquals("TRACK-100", result.trackingNumber());
        verify(mockSupplier, times(1)).processOrder(any(OrderPayload.class));
    }

    @Test
    void testVIPOrderAddsSamplesAndRetriesOnFailure() {
        when(mockSupplier.processOrder(any()))
                .thenReturn(OrderFulfillmentResult.fail("Stock Busy"))
                .thenReturn(OrderFulfillmentResult.ok("TRACK-VIP-99"));

        VIPBeautyBoxOrder order = new VIPBeautyBoxOrder(mockSupplier);
        OrderFulfillmentResult result = order.checkout("VIP-CUST", List.of("FACE-CREAM"), 150.00, CosmeticCategory.SKINCARE);

        assertTrue(result.success());
        assertEquals("TRACK-VIP-99", result.trackingNumber());
        verify(mockSupplier, times(2)).processOrder(any(OrderPayload.class));
    }

    @Test
    void testAdapterSuccessTranslation() throws Exception {
        when(mockLegacyApi.executeBatchSupplyRequest(any(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(200);

        LegacySupplierAdapter adapter = new LegacySupplierAdapter(mockLegacyApi);
        OrderFulfillmentResult result = adapter.processOrder(
                new OrderPayload("CUST-B2B", List.of("PERFUME-50ML"), 120.00, CosmeticCategory.PERFUME)
        );

        assertTrue(result.success());
        assertNotNull(result.trackingNumber());
    }

    @Test
    void testAdapterStatusCodeTranslation() throws Exception {
        when(mockLegacyApi.executeBatchSupplyRequest(any(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(400);

        LegacySupplierAdapter adapter = new LegacySupplierAdapter(mockLegacyApi);
        OrderFulfillmentResult result = adapter.processOrder(
                new OrderPayload("", List.of("PERFUME-50ML"), 120.00, CosmeticCategory.PERFUME)
        );

        assertFalse(result.success());
        assertEquals("B2B Supplier Error Code: 400", result.errorMessage());
    }

    @Test
    void testAdapterExceptionTranslation() throws Exception {
        when(mockLegacyApi.executeBatchSupplyRequest(any(), anyInt(), anyString(), anyBoolean()))
                .thenThrow(new LegacyB2BConnectionException("Server Offline"));

        LegacySupplierAdapter adapter = new LegacySupplierAdapter(mockLegacyApi);
        OrderFulfillmentResult result = adapter.processOrder(
                new OrderPayload("OFFLINE_CUST", List.of("CREAM"), 50.00, CosmeticCategory.SKINCARE)
        );

        assertFalse(result.success());
        assertEquals("B2B Network Failure: Server Offline", result.errorMessage());
    }

    @Test
    void testDynamicSupplierSelection() {
        DynamicSupplierSelector selector = new DynamicSupplierSelector(mockLegacyApi);

        assertInstanceOf(LegacySupplierAdapter.class, selector.selectSupplier(CosmeticCategory.ORGANIC, "CUST-1"));
        assertInstanceOf(LegacySupplierAdapter.class, selector.selectSupplier(CosmeticCategory.MAKEUP, "B2B_42"));
        assertInstanceOf(PartnerRetailSupplier.class, selector.selectSupplier(CosmeticCategory.MAKEUP, "PARTNER_7"));
        assertInstanceOf(InternalWarehouseSupplier.class, selector.selectSupplier(CosmeticCategory.SKINCARE, "CUST-1"));
    }
}
