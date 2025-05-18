package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.FindAllProductsService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindAllProductsServiceImpl implements FindAllProductsService {

    private final ProductRepository productRepository;

    public FindAllProductsServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}
