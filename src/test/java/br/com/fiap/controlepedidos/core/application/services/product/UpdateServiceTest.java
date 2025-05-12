package br.com.fiap.controlepedidos.core.application.services.product;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.ports.ProductRepository;
import br.com.fiap.controlepedidos.core.application.services.product.impl.UpdateProductServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceTest {

    private ProductRepository productRepository;
    private UpdateProductServiceImpl updateImpl;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        updateImpl = new UpdateProductServiceImpl(productRepository);
    }

    @Test
    void testUpdate_WhenProductExists_ShouldUpdateAndReturnProduct() {
        UUID id = UUID.randomUUID();

        Product persistedProduct = new Product(UUID.randomUUID(), "Produto Teste", 1999,
                Category.LANCHE, "Hamburguer", true, "");

        ProductDTO updateDTO = new ProductDTO(
                id,
                "Atualizado",
                2000,
                Category.LANCHE,
                "Nova descrição",
                false,
                "atualizado.png"
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(persistedProduct));

        Product updatedProduct = updateImpl.update(id, updateDTO);

        assertEquals("Atualizado", updatedProduct.getName());
        assertEquals(2000, updatedProduct.getPrice());
        assertEquals(Category.LANCHE, updatedProduct.getCategory());
        assertEquals("Nova descrição", updatedProduct.getDescription());
        assertFalse(updatedProduct.isActive());
        assertEquals("atualizado.png", updatedProduct.getImage());

        verify(productRepository).save(persistedProduct);
    }

    @Test
    void testUpdate_WhenProductNotFound_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        ProductDTO updateDTO = new ProductDTO(id, "Teste", 1899, Category.LANCHE, "Desc", true, "img.png");

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> updateImpl.update(id, updateDTO)
        );

        assertEquals("Produto com ID %s não encontrado.".formatted(updateDTO.id()), thrown.getMessage());
    }

}
