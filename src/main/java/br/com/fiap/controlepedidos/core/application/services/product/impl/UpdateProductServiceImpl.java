package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.UpdateProductService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProductServiceImpl implements UpdateProductService {

    private final ProductRepository productRepository;

    public UpdateProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product update(UUID id, ProductDTO product) {

        Product persistedProduct = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Produto com ID %s não encontrado.".formatted(product.id())));

        persistedProduct.setName(product.name() != null ? product.name() : persistedProduct.getName());
        persistedProduct.setPrice(persistedProduct.getPrice());
        persistedProduct.setCategory(product.category() != null ? product.category() : persistedProduct.getCategory());
        persistedProduct.setDescription(product.description() != null ? product.description() : persistedProduct.getDescription());
        persistedProduct.setActive(product.active() != null ? product.active() : persistedProduct.isActive());
        persistedProduct.setImage(product.image() != null ? product.image() : persistedProduct.getImage());

        productRepository.save(persistedProduct);

        return persistedProduct;
    }

}
