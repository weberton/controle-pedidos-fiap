package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.FindProductByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindProductByIdServiceImpl implements FindProductByIdService {

    private final ProductRepository productRepository;

    public FindProductByIdServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Produto com ID %s não encontrado.".formatted(id)));
    }

}
