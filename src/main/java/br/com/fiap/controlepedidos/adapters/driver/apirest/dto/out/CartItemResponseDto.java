package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.core.domain.entities.CartItem;

import java.util.UUID;

public record CartItemResponseDto(UUID productId,
                                  String name,
                                  int priceCents,
                                  int quantity,
                                  int subtotalCents) {

    public static CartItemResponseDto fromDomain(CartItem cartItem) {
        return new CartItemResponseDto(cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getPriceCents(),
                cartItem.getQuantity(),
                cartItem.getSubtotalCents());
    }
}
