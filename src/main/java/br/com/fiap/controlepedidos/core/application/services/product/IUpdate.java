package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.domain.entities.Product;

public interface IUpdate {

    Product update(ProductDTO product);

}
