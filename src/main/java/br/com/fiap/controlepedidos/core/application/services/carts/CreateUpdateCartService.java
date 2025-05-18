package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;

import java.util.UUID;

public interface CreateUpdateCartService {
    Cart create(CartItem cartItem);

    Cart addItem(UUID cartId, CartItem cartItem);

    Cart removeItem(UUID cartId, UUID itemId);

    Cart updateQuantity(UUID cartId, UUID itemId, int quantity);
}
