package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.FindProductByIdServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindByIdServiceTest {

    private ProductRepository productRepository;
    private FindProductByIdServiceImpl findById;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        findById = new FindProductByIdServiceImpl(productRepository);
    }

    @Test
    void testFindById_WhenProductExists_ShouldReturnProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Produto Teste", 1999,
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
