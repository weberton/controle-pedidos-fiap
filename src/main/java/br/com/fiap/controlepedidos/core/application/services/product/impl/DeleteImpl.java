package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.IDeleteById;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteImpl implements IDeleteById {

    private final IProductRepository productRepository;

    public DeleteImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void delete(UUID id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            Product product1 = product.get();
            product1.setActive(false);
            productRepository.save(product1);
        }
    }
}
