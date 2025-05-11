package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in;


import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record CreateItemRequest(@NotEmpty(message = NULL_PRODUCT_ID) String productId,
                                @Min(value = 1, message = INVALID_QUANTITY) int quantity) {

    public static final String INVALID_QUANTITY = "Quantidade deve ser pelo menos 1.";
    public static final String NULL_PRODUCT_ID = "ID do produto não pode ser nulo.";

    public CartItem toDomain() {
        Product product = Product.builder().id(UUID.fromString(productId)).build();
        return CartItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }
}
