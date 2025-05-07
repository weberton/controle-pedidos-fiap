package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.IFindById;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindByIdImpl implements IFindById {

    private final IProductRepository productRepository;

    public FindByIdImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Produto com ID " + id + " não encontrado."));
    }

}
