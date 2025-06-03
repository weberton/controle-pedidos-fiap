package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.DeleteProductImplService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteServiceTest {

    private ProductRepository productRepository;
    private DeleteProductImplService deleteImpl;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        deleteImpl = new DeleteProductImplService(productRepository);
    }

    @Test
    void testDelete_WhenProductExists_ShouldSetInactiveAndSave() {
        UUID id = UUID.randomUUID();
        Product product = new Product(UUID.randomUUID(), "Produto Teste", 1999,
                Category.LANCHE, "Hamburguer", true, "");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        deleteImpl.delete(id);

        assertFalse(product.isActive());
        verify(productRepository).delete(product);
    }

    @Test
    void testDelete_WhenProductDoesNotExist_ShouldDoNothing() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        deleteImpl.delete(id);

        verify(productRepository, never()).save(any());
    }

}