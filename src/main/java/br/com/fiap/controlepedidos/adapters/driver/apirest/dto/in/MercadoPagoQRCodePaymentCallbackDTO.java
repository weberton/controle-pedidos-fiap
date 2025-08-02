package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MercadoPagoQRCodePaymentCallbackDTO(@NotNull(message = ORDER_ID_REQUIRED) UUID orderId,
                                                  @NotNull(message = PAID_VALUE_REQUIRED) Integer paidValue) {
    private static final String ORDER_ID_REQUIRED = "ID da ordem não pode ser nulo";
    private static final String PAID_VALUE_REQUIRED = "O valor pago não foi informado";
}

