package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartAssociateCustomerRequest(@NotNull(message = CUSTOMER_ID_REQUIRED) UUID customerId) {
    private static final String CUSTOMER_ID_REQUIRED = "ID to cliente não pode ser nulo.";
}
