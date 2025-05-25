package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.core.domain.entities.CartItem;

import java.util.UUID;

public record CartItemResponseDto(UUID id,
                                  UUID productId,
                                  String name,
                                  float priceCents,
                                  int quantity,
                                  double subtotalCents) {

    public static CartItemResponseDto fromDomain(CartItem cartItem) {
        return new CartItemResponseDto(cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getPriceCents(),
                cartItem.getQuantity(),
                cartItem.getSubtotalCents());
    }
}
