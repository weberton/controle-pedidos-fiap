package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.domain.entities.Product;

import java.util.UUID;

public interface UpdateProductService {

    Product update(UUID id, ProductDTO product);

}
