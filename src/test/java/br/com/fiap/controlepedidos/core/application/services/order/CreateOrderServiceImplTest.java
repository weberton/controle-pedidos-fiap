package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.CreateOrderServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceImplTest {
    @Mock
    private FindCartService findCartService;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private CreateOrderServiceImpl createOrderService;

    private UUID cartId;
    private Cart dummyCart;
    private Customer dummyCustomer;

    @BeforeEach
    void setup() {
        cartId = UUID.randomUUID();

        dummyCustomer = new Customer();
        dummyCustomer.setId(UUID.randomUUID());
        dummyCustomer.setName("Joao");
        dummyCustomer.setCpf("98765432100");
        dummyCustomer.setEmail("joao@gmail.com");

        dummyCart = new Cart();
        dummyCart.setId(cartId);
        dummyCart.setCustomer(dummyCustomer);
        dummyCart.setTotalCents(5_000);

        when(findCartService.findById(cartId)).thenReturn(dummyCart);
    }

    @Test
    void createOrder_shouldPopulateOrderAndCallSave() {
        Order returnedOrder = createOrderService.createOrder(cartId);

        assertSame(dummyCart, returnedOrder.getCart());
        assertSame(dummyCustomer, returnedOrder.getCustomer());
        assertThat(returnedOrder.getTotalCents()).isEqualTo(5_000);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertSame(dummyCart, savedOrder.getCart());
        assertSame(dummyCustomer, savedOrder.getCustomer());
        assertThat(savedOrder.getTotalCents()).isEqualTo(5_000L);

        assertSame(savedOrder, returnedOrder);
    }
}
