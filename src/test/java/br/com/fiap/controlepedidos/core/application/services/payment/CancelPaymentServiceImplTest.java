package br.com.fiap.controlepedidos.core.application.services.payment;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.impl.CancelPaymentServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelPaymentServiceImplTest {

    private IPaymentRepository paymentRepository;
    private CancelPaymentServiceImpl cancelPaymentService;

    @BeforeEach
    void setup() {
        paymentRepository = mock(IPaymentRepository.class);
        cancelPaymentService = new CancelPaymentServiceImpl(paymentRepository);
    }

    @Test
    void cancelPayment_withValidOrder_shouldCancelAndSave() {

        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);

        Payment payment = spy(new Payment());
        payment.setOrder(order);

        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = cancelPaymentService.cancelPayment(payment);

        verify(payment).cancelPayment();
        verify(paymentRepository).save(payment);
        assertThat(result).isEqualTo(payment);
    }

    @Test
    void cancelPayment_withNullOrder_shouldThrowException() {
        Payment payment = new Payment(); // getOrder() returns null

        assertThatThrownBy(() -> cancelPaymentService.cancelPayment(payment))
                .isInstanceOf(PaymentWithoutOrderException.class)
                .hasMessage("Um pagamento deve estar vinculado a um pedido.");

        verify(paymentRepository, never()).save(any());
    }

}
