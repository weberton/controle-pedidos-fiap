package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.DeleteProductByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeleteProductImplService implements DeleteProductByIdService {

    private final ProductRepository productRepository;

    public DeleteProductImplService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void delete(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            productRepository.save(product);
        }
    }
}
