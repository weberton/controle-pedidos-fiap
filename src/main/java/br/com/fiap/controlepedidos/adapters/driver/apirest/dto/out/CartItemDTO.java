package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;

import java.util.UUID;

public record CartItemDTO(
        UUID id,
        int quantity,
        int priceCents,
        int subtotalCents,
        ProductDTO product
) {
    public static CartItemDTO from(CartItem item) {
        return new CartItemDTO(
                item.getId(),
                item.getQuantity(),
                item.getPriceCents(),
                item.getSubtotalCents(),
                ProductDTO.convertToDTO(item.getProduct())
        );
    }
}