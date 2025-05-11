package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.application.ports.IProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.DeleteImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteImplTest {

    private IProductRepository productRepository;
    private DeleteImpl deleteImpl;

    @BeforeEach
    void setUp() {
        productRepository = mock(IProductRepository.class);
        deleteImpl = new DeleteImpl(productRepository);
    }

    @Test
    void testDelete_WhenProductExists_ShouldSetInactiveAndSave() {
        UUID id = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Produto Teste", new BigDecimal("19.99"),
                Category.LANCHE, "Hamburguer", true, "");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        deleteImpl.delete(id);

        assertFalse(product.isActive());
        verify(productRepository).save(product);
    }

    @Test
    void testDelete_WhenProductDoesNotExist_ShouldDoNothing() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        deleteImpl.delete(id);

        verify(productRepository, never()).save(any());
    }

}