package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.GetAllOrdersReadyToPrepareServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
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
class GetAllOrdersReadyToPrepareServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @InjectMocks
    private GetAllOrdersReadyToPrepareServiceImpl service;

    @Test
    void getAll_ShouldReturnOrdersWithCustomersLoaded() {
        // Arrange
        UUID customerId1 = UUID.randomUUID();
        Customer customer1 = new Customer();
        customer1.setId(customerId1);
        customer1.setName("Customer One");

        UUID customerId2 = UUID.randomUUID();
        Customer customer2 = new Customer();
        customer2.setId(customerId2);
        customer2.setName("Customer Two");

        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setOrderStatus(OrderStatus.RECEIVED);
        order1.setCustomer(new Customer());
        order1.getCustomer().setId(customerId1);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setOrderStatus(OrderStatus.RECEIVED);
        order2.setCustomer(new Customer());
        order2.getCustomer().setId(customerId2);

        List<Order> ordersList = List.of(order1, order2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> ordersPage = new PageImpl<>(ordersList, pageable, ordersList.size());

        when(orderRepository.findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.RECEIVED, pageable))
                .thenReturn(ordersPage);
        when(findCustomerByIdService.findById(customerId1)).thenReturn(customer1);
        when(findCustomerByIdService.findById(customerId2)).thenReturn(customer2);

        Page<Order> result = service.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(Order::getCustomer)
                .extracting(Customer::getName)
                .containsExactly("Customer One", "Customer Two");

        verify(orderRepository, times(1)).findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.RECEIVED, pageable);
        verify(findCustomerByIdService, times(1)).findById(customerId1);
        verify(findCustomerByIdService, times(1)).findById(customerId2);
    }
}