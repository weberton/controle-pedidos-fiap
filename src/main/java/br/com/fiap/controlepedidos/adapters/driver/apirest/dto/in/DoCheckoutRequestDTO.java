package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DoCheckoutRequestDTO(@NotNull(message = CART_ID_REQUIRED) UUID cartId) {
    private static final String CART_ID_REQUIRED = "ID do carrinho não pode ser nulo";
}
