package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.GetAllOrdersServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllOrdersServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private FindCustomerByIdService customerService;

    @InjectMocks
    private GetAllOrdersServiceImpl service;

    @Test
    void getAll_ShouldReturnOrdersWithCustomerAndCart() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Customer Test");

        Cart cart = new Cart();

        Order orderWithCustomerAndCart = new Order();
        orderWithCustomerAndCart.setId(UUID.randomUUID());
        orderWithCustomerAndCart.setCustomer(new Customer());
        orderWithCustomerAndCart.getCustomer().setId(customerId);
        orderWithCustomerAndCart.setCart(cart);

        Order orderWithoutCustomerOrCart = new Order();
        orderWithoutCustomerOrCart.setId(UUID.randomUUID());

        List<Order> ordersList = List.of(orderWithCustomerAndCart, orderWithoutCustomerOrCart);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> ordersPage = new PageImpl<>(ordersList, pageable, ordersList.size());

        when(orderRepository.findAll(pageable)).thenReturn(ordersPage);
        when(customerService.findById(customerId)).thenReturn(customer);

         Page<Order> result = service.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);


        Order firstOrder = result.getContent().get(0);
        assertThat(firstOrder.getCustomer()).isNotNull();
        assertThat(firstOrder.getCustomer().getName()).isEqualTo("Customer Test");

        assertThat(firstOrder.getCart()).isEqualTo(cart);

        Order secondOrder = result.getContent().get(1);
        assertThat(secondOrder.getCustomer()).isNull();
        assertThat(secondOrder.getCart()).isNull();

        verify(orderRepository, times(1)).findAll(pageable);
        verify(customerService, times(1)).findById(customerId);
    }
}