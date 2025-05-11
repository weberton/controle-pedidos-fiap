package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.FindByIdImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindByIdImplTest {

    private IProductRepository productRepository;
    private FindByIdImpl findById;

    @BeforeEach
    void setUp() {
        productRepository = mock(IProductRepository.class);
        findById = new FindByIdImpl(productRepository);
    }

    @Test
    void testFindById_WhenProductExists_ShouldReturnProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Produto Teste", new BigDecimal("19.99"),
                Category.LANCHE, "Hamburguer", true, "");
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = findById.findById(id);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
    }

    @Test
    void testFindById_WhenProductDoesNotExist_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> findById.findById(id)
        );

        assertEquals("Produto com ID %s não encontrado.".formatted(id), thrown.getMessage());
    }

}
