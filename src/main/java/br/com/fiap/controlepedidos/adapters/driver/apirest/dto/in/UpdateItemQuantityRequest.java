package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import jakarta.validation.constraints.Min;

public record UpdateItemQuantityRequest(@Min(value = 0, message = INVALID_QUANTITY) int quantity) {
    public static final String INVALID_QUANTITY = "Quantidade deve ser maior ou igual a zero.";
}