package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.ICreate;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.stereotype.Service;

@Service
public class CreateImpl implements ICreate {

    private final IProductRepository productRepository;

    public CreateImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(ProductDTO productDTO) {
        return productRepository.save(productDTO.convertToModel());
    }

}
