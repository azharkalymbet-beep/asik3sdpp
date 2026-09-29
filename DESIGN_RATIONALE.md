# Design Rationale Document

## 1. Chosen Problem Domain
The domain is a **Cosmetics & Beauty E-Commerce Order Fulfillment Platform**. The system formats cosmetic orders (`StandardCosmeticOrder` vs `VIPBeautyBoxOrder` with complimentary samples) and dispatches them across fulfillment channels (Internal Warehouse, Partner Retail Stores, and an external Organic Brand B2B Supplier).

## 2. Complexity Module Selection
**Choice:** *Dynamic Implementor Selection*.
The fulfillment channel is chosen at runtime by `DynamicSupplierSelector` based on order parameters (`CosmeticCategory` and customer type prefix).

## 3. Architectural Rationale: Bridge + Adapter
- **Why Bridge alone is not enough:** Bridge decouples order types (`CosmeticOrder`) from fulfillment mechanics (`CosmeticSupplier`). But the supply chain must integrate an external legacy API (`LegacyBrandB2BApi`) that cannot implement `CosmeticSupplier` directly because its signature, parameters and error mechanisms are incompatible.
- **Why Adapter alone is not enough:** Adapter wraps `LegacyBrandB2BApi`, but does not address the two-axis variation of order types and suppliers. Without Bridge, adding a new order type across N suppliers causes an M x N subclass explosion.
- **Incompatibility of the wrapped class:**
  1. **Signature & types:** expects `(String[] rawItemCodes, int amountInCents, String clientCode, boolean isPriority)` instead of a unified `OrderPayload`.
  2. **Error protocol:** uses integer status codes (`200` success, others failure) and throws `LegacyB2BConnectionException`, whereas the system expects an `OrderFulfillmentResult`.

## 4. Open/Closed Principle Compliance
- **Abstraction axis:** adding a new order type (e.g. `SubscriptionCosmeticOrder`) requires zero changes to existing suppliers.
- **Implementor axis:** adding a new supplier (e.g. `DropshippingSupplier`) requires zero changes to existing orders.

## 5. Limitation of Final Design
Translating errors into generic `OrderFulfillmentResult` strips lower-level B2B stack traces, which may require secondary logging for deep debugging.
