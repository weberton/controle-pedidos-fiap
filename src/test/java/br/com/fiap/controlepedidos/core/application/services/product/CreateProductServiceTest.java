package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.CreateProductServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    private ProductRepository productRepository;
    private CreateProductServiceImpl createProductService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        createProductService = new CreateProductServiceImpl(productRepository);
    }

    @Test
    void shouldCreateProductFromDTO() {
        ProductDTO productDTO = mock(ProductDTO.class);
        Product productModel = new Product(UUID.randomUUID(), "Produto Teste", 799,
                Category.BEBIDA, "Sprite", true, "");

        when(productDTO.convertToModel()).thenReturn(productModel);
        when(productRepository.save(productModel)).thenReturn(productModel);

        Product createdProduct = createProductService.create(productDTO);

        assertEquals(productModel, createdProduct);
        verify(productDTO, times(1)).convertToModel();
        verify(productRepository, times(1)).save(productModel);
    }

}