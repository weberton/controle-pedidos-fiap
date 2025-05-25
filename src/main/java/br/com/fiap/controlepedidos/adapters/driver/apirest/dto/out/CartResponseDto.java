package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.core.domain.entities.Cart;

import java.util.List;
import java.util.UUID;

public record CartResponseDto(UUID cartId,
                              UUID customerId,
                              List<CartItemResponseDto> items,
                              float totalCents) {

    public static CartResponseDto fromDomain(Cart cart) {
        return new CartResponseDto(cart.getId(),
                cart.getCustomer() == null ? null : cart.getCustomer().getId(),
                cart.getItems().stream().map(CartItemResponseDto::fromDomain).toList(),
                cart.getTotalCents());
    }
}
