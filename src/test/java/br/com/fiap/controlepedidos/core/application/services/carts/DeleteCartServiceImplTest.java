package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.impl.DeleteCartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DeleteCartServiceImplTest {
    @Mock
    private CartsRepository cartsRepository;
    @InjectMocks
    private DeleteCartServiceImpl deleteCartService;

    @Test
    void deleteById() {
        var cartId = UUID.randomUUID();
        deleteCartService.deleteById(cartId);
        Mockito.verify(cartsRepository).deleteById(cartId);
    }
}
