package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.core.domain.entities.Product;

import java.util.UUID;

public interface FindProductByIdService {

    Product findById(UUID id);

}
