package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.impl.PayOrderServiceImpl;
import br.com.fiap.controlepedidos.core.application.services.payment.IConfirmPaymentService;
import br.com.fiap.controlepedidos.core.application.services.payment.IFindPaymentByOrderId;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PayOrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IConfirmPaymentService confirmPaymentService;

    @Mock
    private FindCustomerByIdService findCustomerByIdService;

    @Mock
    private IFindPaymentByOrderId findPaymentByOrderId;

    @InjectMocks
    private PayOrderServiceImpl payOrderService;

    private Order order;
    private Payment payment;
    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());

        order = Mockito.mock(Order.class);
        when(order.getId()).thenReturn(UUID.randomUUID());
        lenient().when(order.getCustomer()).thenReturn(customer);

        payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.WAITING);
    }


    @Test
    void payOrder_whenOrderIsPaid_shouldConfirmPaymentAndSaveOrder() {
        int receivedValue = 1000;

        when(findPaymentByOrderId.findPayment(order.getId())).thenReturn(payment);
        when(order.payOrder(receivedValue)).thenReturn(true); // order is paid
        when(confirmPaymentService.confirmPayment(payment)).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setPaymentStatus(PaymentStatus.PAID);
            return p;
        });
        when(findCustomerByIdService.findById(customer.getId())).thenReturn(customer);

        Payment result = payOrderService.payOrder(order, receivedValue);

        assertThat(result).isNotNull();
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(result.getTotalCents()).isEqualTo(receivedValue);

        verify(findPaymentByOrderId, times(1)).findPayment(order.getId());
        verify(order, times(1)).payOrder(receivedValue);
        verify(confirmPaymentService, times(1)).confirmPayment(payment);
        verify(orderRepository, times(1)).save(order);
        verify(findCustomerByIdService, times(1)).findById(customer.getId());
        verify(order, times(1)).setCustomer(customer);
    }

    @Test
    void payOrder_whenOrderIsNotPaid_shouldNotConfirmPaymentOrSave() {
        int receivedValue = 1000;

        when(findPaymentByOrderId.findPayment(order.getId())).thenReturn(payment);
        when(order.payOrder(receivedValue)).thenReturn(false);

        Payment result = payOrderService.payOrder(order, receivedValue);

        assertThat(result).isEqualTo(payment);
        assertThat(result.getPaymentStatus()).isNotEqualTo(PaymentStatus.PAID);

        verify(findPaymentByOrderId, times(1)).findPayment(order.getId());
        verify(order, times(1)).payOrder(receivedValue);
        verify(confirmPaymentService, never()).confirmPayment(any());
        verify(orderRepository, never()).save(any());
        verify(findCustomerByIdService, never()).findById(any());
    }

    @Test
    void payOrder_whenOrderHasNoCustomer_shouldSaveOrderAndSetCustomerNull() {
        int receivedValue = 1000;

        when(order.getCustomer()).thenReturn(null);
        when(findPaymentByOrderId.findPayment(order.getId())).thenReturn(payment);
        when(order.payOrder(receivedValue)).thenReturn(true);
        when(confirmPaymentService.confirmPayment(payment)).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setPaymentStatus(PaymentStatus.PAID);
            return p;
        });

        Payment result = payOrderService.payOrder(order, receivedValue);

        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);

        verify(orderRepository, times(1)).save(order);
        verify(order, times(1)).setCustomer(null);
        verify(findCustomerByIdService, never()).findById(any());
    }
}