package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.GetAllOrdersInProcessServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllOrdersInProcessServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @InjectMocks
    private GetAllOrdersInProcessServiceImpl service;

    @Test
    void getAllOrdersInProcess_ShouldFilterSortPaginateAndLoadCustomers() {
        // Arrange
        Customer customer1 = new Customer();
        UUID customerId1 = UUID.randomUUID();
        customer1.setId(customerId1);
        customer1.setName("Customer A");

        Customer customer2 = new Customer();
        UUID customerId2 = UUID.randomUUID();
        customer2.setId(customerId2);
        customer2.setName("Customer B");

        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setOrderStatus(OrderStatus.READY);
        order1.setCreatedAt(OffsetDateTime.now().minusDays(3));
        order1.setCustomer(new Customer());
        order1.getCustomer().setId(customerId1);

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setOrderStatus(OrderStatus.INPREP);
        order2.setCreatedAt(OffsetDateTime.now().minusDays(2));
        order2.setCustomer(new Customer());
        order2.getCustomer().setId(customerId2);

        Order order3 = new Order();
        order3.setId(UUID.randomUUID());
        order3.setOrderStatus(OrderStatus.RECEIVED);
        order3.setCreatedAt(OffsetDateTime.now().minusDays(1));
        order3.setCustomer(null);

        Order order4 = new Order();
        order4.setId(UUID.randomUUID());
        order4.setOrderStatus(OrderStatus.DONE);
        order4.setCreatedAt(OffsetDateTime.now());

        Order order5 = new Order();
        order5.setId(UUID.randomUUID());
        order5.setOrderStatus(OrderStatus.CREATED);
        order5.setCreatedAt(OffsetDateTime.now());

        List<Order> allOrders = List.of(order1, order2, order3, order4, order5);

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(findCustomerByIdService.findById(customerId1)).thenReturn(customer1);
        when(findCustomerByIdService.findById(customerId2)).thenReturn(customer2);

        Pageable pageable = PageRequest.of(0, 10);


        Page<Order> resultPage = service.getAllOrdersInProcess(pageable);


        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(3);
        assertThat(resultPage.getContent()).hasSize(3);


        assertThat(resultPage.getContent().get(0).getOrderStatus()).isEqualTo(OrderStatus.READY);
        assertThat(resultPage.getContent().get(1).getOrderStatus()).isEqualTo(OrderStatus.INPREP);
        assertThat(resultPage.getContent().get(2).getOrderStatus()).isEqualTo(OrderStatus.RECEIVED);


        assertThat(resultPage.getContent().get(0).getCustomer().getName()).isEqualTo("Customer A");
        assertThat(resultPage.getContent().get(1).getCustomer().getName()).isEqualTo("Customer B");
        assertThat(resultPage.getContent().get(2).getCustomer()).isNull();


        verify(orderRepository, times(1)).findAll();
        verify(findCustomerByIdService, times(1)).findById(customerId1);
        verify(findCustomerByIdService, times(1)).findById(customerId2);
    }
}