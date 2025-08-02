package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.FinishOrderPreparationServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinishOrderPreparationServiceImplTest {

    @Mock
    private FindOrderByIdService findOrderByIdService;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @InjectMocks
    private FinishOrderPreparationServiceImpl service;

    private UUID orderId;

    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();
    }

    @Test
    void perform_ShouldFinishPreparation_SaveOrder_AndUpdateCustomer_WhenCustomerExists() {

        Customer originalCustomer = new Customer();
        originalCustomer.setId(UUID.randomUUID());
        Order order = new Order();
        order.setId(orderId);
        order.setCustomer(originalCustomer);

        Customer freshCustomer = new Customer();
        freshCustomer.setId(originalCustomer.getId());
        freshCustomer.setName("Updated Customer");

        when(findOrderByIdService.getById(orderId)).thenReturn(order);
        when(findCustomerByIdService.findById(originalCustomer.getId())).thenReturn(freshCustomer);

        Order result = service.perform(orderId);
        verify(findOrderByIdService).getById(orderId);
        verify(orderRepository).save(order);
        verify(findCustomerByIdService).findById(originalCustomer.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCustomer()).isEqualTo(freshCustomer);
    }

    @Test
    void perform_ShouldFinishPreparation_AndSaveOrder_WhenNoCustomer() {

        Order order = new Order();
        order.setId(orderId);
        order.setCustomer(null);

        when(findOrderByIdService.getById(orderId)).thenReturn(order);

        Order result = service.perform(orderId);

        verify(findOrderByIdService).getById(orderId);
        verify(orderRepository).save(order);
        verifyNoInteractions(findCustomerByIdService);

        assertThat(result).isNotNull();
        assertThat(result.getCustomer()).isNull();
    }
}
