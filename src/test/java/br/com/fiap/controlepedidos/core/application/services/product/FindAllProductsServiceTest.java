package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.FindAllProductsServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllProductsServiceTest {

    private ProductRepository productRepository;
    private FindAllProductsServiceImpl findAllProductsService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        findAllProductsService = new FindAllProductsServiceImpl(productRepository);
    }

    @Test
    void shouldReturnPaginatedProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        List<Product> products = List.of(
                new Product(UUID.randomUUID(), "Produto Teste", 1999,
                        Category.LANCHE, "Hamburguer", true, ""),
                new Product(UUID.randomUUID(), "Produto Teste II", 899,
                        Category.SOBREMESA, "Sorvete de flocos", true, "")
        );

        Page<Product> expectedPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<Product> result = findAllProductsService.findAll(pageable);

        assertEquals(expectedPage, result);
        verify(productRepository, times(1)).findAll(pageable);
    }

}
