package br.com.fiap.controlepedidos.core.application.services.carts;

import java.util.UUID;

public interface DeleteCartService {

    void deleteById(UUID cartId);

}
