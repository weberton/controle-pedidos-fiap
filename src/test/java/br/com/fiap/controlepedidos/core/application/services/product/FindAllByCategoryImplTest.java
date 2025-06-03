package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.FindAllByCategoryImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllByCategoryImplTest {

    private ProductRepository productRepository;
    private FindAllByCategoryImpl service;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        service = new FindAllByCategoryImpl(productRepository);
    }

    @Test
    void shouldReturnPagedResponseOfProductDTOsWhenCategoryIsValid() {

        String categoryStr = "lanche";
        PageRequest pageRequest = PageRequest.of(0, 2);

        Product dto1 = new Product(UUID.randomUUID(), "X-Tudo", 4000, Category.LANCHE, "All in", true, null);
        Product dto2 = new Product(UUID.randomUUID(), "X-Salada", 2000, Category.LANCHE, "O basico", true, null);

        Page<Product> productPage = new PageImpl<>(List.of(dto1, dto2), pageRequest, 2);

        when(productRepository.findByCategory(eq(Category.LANCHE), eq(pageRequest))).thenReturn(productPage);

        PagedResponse<ProductDTO> response = service.findAll(categoryStr, pageRequest);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("X-Tudo", response.getContent().getFirst().name());
        verify(productRepository, times(1)).findByCategory(Category.LANCHE, pageRequest);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCategoryIsInvalid() {

        String invalidCategory = "invalid";

        assertThrows(IllegalArgumentException.class, () ->
                service.findAll(invalidCategory, PageRequest.of(0, 10))
        );

        verifyNoInteractions(productRepository);
    }

}
