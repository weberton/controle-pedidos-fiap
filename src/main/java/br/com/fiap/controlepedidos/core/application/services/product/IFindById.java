package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.core.domain.entities.Product;

import java.util.UUID;

public interface IFindById {

    Product findById(UUID id);

}
