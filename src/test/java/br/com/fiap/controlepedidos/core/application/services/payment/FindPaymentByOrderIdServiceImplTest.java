package br.com.fiap.controlepedidos.core.application.services.payment;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.impl.FindPaymentByOrderIdServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

class FindPaymentByOrderIdServiceImplTest {

    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private FindPaymentByOrderIdServiceImpl findPaymentByOrderIdService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findPayment_shouldReturnPayment_whenOrderIdIsNotNull() {
        UUID orderId = UUID.randomUUID();
        Payment expectedPayment = new Payment();

        when(paymentRepository.findWithOrderByOrderId(orderId)).thenReturn(expectedPayment);

        Payment actualPayment = findPaymentByOrderIdService.findPayment(orderId);

        assertThat(actualPayment).isSameAs(expectedPayment);
        verify(paymentRepository, times(1)).findWithOrderByOrderId(orderId);
    }

    @Test
    void findPayment_shouldReturnNewPayment_whenOrderIdIsNull() {
        Payment actualPayment = findPaymentByOrderIdService.findPayment(null);

        assertThat(actualPayment).isNotNull();
        verify(paymentRepository, never()).findWithOrderByOrderId(any());
    }
}