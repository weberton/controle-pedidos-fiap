package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.IUpdate;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UpdateImpl implements IUpdate {

    private final IProductRepository productRepository;

    public UpdateImpl(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product update(ProductDTO product) {

        Product persistedProduct = productRepository.findById(product.id())
                .orElseThrow(() -> new RecordNotFoundException("Produto com ID %s não encontrado.".formatted(product.id())));

        persistedProduct.setName(product.name() != null ? product.name() : persistedProduct.getName());
        persistedProduct.setPrice(product.name() != null ? product.price() : persistedProduct.getPrice());
        persistedProduct.setCategory(product.name() != null ? product.category() : persistedProduct.getCategory());
        persistedProduct.setDescription(product.name() != null ? product.description() : persistedProduct.getDescription());
        persistedProduct.setActive(product.name() != null ? product.active() : persistedProduct.isActive());
        persistedProduct.setImage(product.name() != null ? product.image() : persistedProduct.getImage());

        productRepository.save(persistedProduct);

        return persistedProduct;
    }

}
