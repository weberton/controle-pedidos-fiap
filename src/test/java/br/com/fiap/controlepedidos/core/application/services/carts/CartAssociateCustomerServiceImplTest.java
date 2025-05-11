package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.impl.CartAssociateCustomerServiceImpl;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.FindCartServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.CartAlreadyAssociatedException;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartAssociateCustomerServiceImplTest {
    @Mock
    private FindCartServiceImpl findCartService;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @Mock
    private CartsRepository cartsRepository;

    @InjectMocks
    private CartAssociateCustomerServiceImpl cartAssociateCustomerService;


    @Test
    void associateCustomer_whenCartDoesNotExist_shouldThrowRecordNotFound() {
        // given
        var cartId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        //When/Then
        when(findCartService.findById(cartId)).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class,
                () -> cartAssociateCustomerService.associateCustomer(cartId, customerId));
    }

    @Test
    void associateCustomer_whenCustomerDoesNotExist_shouldThrowRecordNotFound() {
        // given
        var customerId = UUID.randomUUID();
        var cartId = UUID.randomUUID();
        var cart = new Cart();
        cart.setId(cartId);

        //When/Then
        when(findCartService.findById(any())).thenReturn(cart);
        when(findCustomerByIdService.findById(customerId)).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class,
                () -> cartAssociateCustomerService.associateCustomer(cartId, customerId));
    }

    @Test
    void associateCustomer_whenCartIsNotAssociatedWithCustomer_shouldAssociateCustomer() {
        // given
        var customer = Customer.builder().id(UUID.randomUUID()).build();

        var cartId = UUID.randomUUID();
        var cart = new Cart();
        cart.setId(cartId);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cart.getId())).thenReturn(cart);
        when(findCustomerByIdService.findById(customer.getId())).thenReturn(customer);

        cartAssociateCustomerService.associateCustomer(cartId, customer.getId());

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        Cart savedCart = cartArgumentCaptor.getValue();
        assertThat(savedCart.getCustomer()).isEqualTo(customer);
    }

    @Test
    void associateCustomer_whenCartIsNotAssociatedWithCustomer_shouldAssociateCustomer__test() {
        // given
        var customerId = UUID.randomUUID();
        var customer2 = Customer.builder().id(UUID.randomUUID()).build();

        var cartId = UUID.randomUUID();
        var cart = new Cart();
        cart.setCustomer(customer2); //cart associated with a different customer
        cart.setId(cartId);

        //When
        when(findCartService.findById(cart.getId())).thenReturn(cart);

        CartAlreadyAssociatedException exception = assertThrows(CartAlreadyAssociatedException.class,
                () -> cartAssociateCustomerService.associateCustomer(cartId, customerId));

        //Then
        assertThat(exception.getMessage()).isEqualTo("Cart %s já está associado a um customer.".formatted(cartId));
    }
}
