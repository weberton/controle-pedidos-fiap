package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;

import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import jakarta.validation.Valid;

import java.util.UUID;

public record CreateCartRequest(UUID customerId,
                                @Valid CreateItemRequest item) {

    public CartItem toDomain() {
        Product product = Product.builder().id(UUID.fromString(item.productId())).build();
        return CartItem.builder()
                .product(product)
                .quantity(item.quantity())
                .build();
    }
}