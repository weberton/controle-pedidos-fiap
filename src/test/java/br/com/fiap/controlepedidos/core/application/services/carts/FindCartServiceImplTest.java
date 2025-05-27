package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.FindCartServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCartServiceImplTest {
    @Mock
    private CartsRepository cartsRepository;
    @InjectMocks
    private FindCartServiceImpl findCartService;

    @Test
    void findById_WhenCartExists_ShouldReturnIt() {
        //Given
        var cartId = UUID.randomUUID();
        var cart = Cart.builder().build();
        cart.setId(cartId);

        //When
        when(cartsRepository.findById(cartId)).thenReturn(Optional.of(cart));
        var result = findCartService.findById(cartId);

        //Then
        assertThat(result).isSameAs(cart);
    }

    @Test
    void findById_WhenCartDoesNotExist_ShouldThrowRecordNotFound() {
        // Given
        UUID missingId = UUID.randomUUID();
        when(cartsRepository.findById(missingId))
                .thenReturn(Optional.empty());

        // When
        RecordNotFoundException ex = assertThrows(
                RecordNotFoundException.class,
                () -> findCartService.findById(missingId)
        );

        //Then
        assertThat(ex.getMessage())
                .isEqualTo("Cart %s not found.".formatted(missingId));
    }
}