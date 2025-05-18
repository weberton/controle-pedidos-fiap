package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllProductsService {

    Page<Product> findAll(Pageable pageable);

}
