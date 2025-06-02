package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.PayOrderServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayOrderServiceTest {
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @InjectMocks
    private PayOrderServiceImpl payOrderService;

    @Test
    void payOrder_ShouldReturnPayment_WhenPaymentIsSuccessful() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);

        Order order = Order.builder()
                .customer(customer)
                .totalCents(1000)
                .build();
        when(findCustomerByIdService.findById(customerId)).thenReturn(customer);

        Payment payment = payOrderService.payOrder(order, 1000);

        assertThat(payment).isNotNull();
        assertThat(payment.getOrder()).isEqualTo(order);

        verify(orderRepository).save(order);
        verify(findCustomerByIdService).findById(customerId);
    }


    @Test
    void payOrder_ShouldReturnEmptyPayment_WhenPaymentIsUnsuccessful() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);

        Order order = Order.builder()
                .customer(customer)
                .totalCents(2000)
                .build();

        Payment payment = payOrderService.payOrder(order, 1000);

        assertThat(payment).isNotNull();
        assertThat(payment.getOrder()).isNull();

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(findCustomerByIdService);
    }

}