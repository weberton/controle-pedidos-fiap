package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.StartOrderPreparationServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartOrderPreparationServiceTest {


    @Mock
    private FindOrderByIdService findOrderByIdService;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private StartOrderPreparationServiceImpl service;

    @Test
    void perform_ShouldStartOrderPreparationAndReturnUpdatedOrder() {

        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(customerId);

        Order order = new Order();
        order.setId(orderId);
        order.setCustomer(customer);

        when(findOrderByIdService.getById(orderId)).thenReturn(order);
        when(findCustomerByIdService.findById(customerId)).thenReturn(customer);


        Order result = service.perform(orderId);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        assertThat(result.getCustomer()).isEqualTo(customer);

        verify(findOrderByIdService).getById(orderId);
        verify(orderRepository).save(order);
        verify(findCustomerByIdService).findById(customerId);
    }

    @Test
    void perform_ShouldThrowException_WhenOrderNotFound() {

        UUID orderId = UUID.randomUUID();
        when(findOrderByIdService.getById(orderId)).thenThrow(new RuntimeException("Order not found"));

        try {
            service.perform(orderId);
        } catch (Exception ex) {
            assertThat(ex.getMessage()).isEqualTo("Order not found");
        }

        verify(findOrderByIdService).getById(orderId);
        verifyNoInteractions(orderRepository, findCustomerByIdService);
    }

}