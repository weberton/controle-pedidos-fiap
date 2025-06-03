package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import org.springframework.data.domain.PageRequest;

public interface FindAllByCategory {

    PagedResponse<ProductDTO> findAll(String category, PageRequest page);

}
