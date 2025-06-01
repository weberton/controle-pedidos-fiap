package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateOrderStatusDTO(@NotNull(message = ORDER_ID_REQUIRED) UUID orderId) {
    private static final String ORDER_ID_REQUIRED = "ID do pedido não pode ser nulo";
}
