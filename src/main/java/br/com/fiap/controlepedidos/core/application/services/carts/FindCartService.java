package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.domain.entities.Cart;

import java.util.UUID;

public interface FindCartService {
    Cart findById(UUID id);
}
