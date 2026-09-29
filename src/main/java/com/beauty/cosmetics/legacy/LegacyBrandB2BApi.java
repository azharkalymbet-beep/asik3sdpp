package com.beauty.cosmetics.legacy;

public class LegacyBrandB2BApi {
    // Несовместимая сигнатура: массив строк, сумма в центах, флаг приоритета.
    // Возвращает статус-код (200 - успех), выбрасывает специфичное исключение.
    public int executeBatchSupplyRequest(String[] rawItemCodes, int amountInCents, String clientCode, boolean isPriority)
            throws LegacyB2BConnectionException {
        if (clientCode == null || clientCode.isEmpty()) {
            return 400; // Invalid Client
        }
        if (clientCode.startsWith("OFFLINE_")) {
            throw new LegacyB2BConnectionException("B2B Mainframe Unreachable");
        }
        return 200; // Success
    }
}
