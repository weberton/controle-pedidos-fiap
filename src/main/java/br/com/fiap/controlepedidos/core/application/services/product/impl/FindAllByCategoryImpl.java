package br.com.fiap.controlepedidos.core.application.services.product.impl;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.FindAllByCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FindAllByCategoryImpl implements FindAllByCategory {

    private final ProductRepository productRepository;

    public FindAllByCategoryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public PagedResponse<ProductDTO> findAll(final String category, final PageRequest page) {

        Category category2 = Category.valueOf(category.trim().toUpperCase());

        return PagedResponse.of(productRepository.findByCategory(category2, page)
                .map(ProductDTO::convertToDTO));
    }

}
