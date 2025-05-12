package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.CreateProductService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.stereotype.Service;

@Service
public class CreateProductServiceImpl implements CreateProductService {

    private final ProductRepository productRepository;

    public CreateProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(ProductDTO productDTO) {
        return productRepository.save(productDTO.convertToModel());
    }

}
