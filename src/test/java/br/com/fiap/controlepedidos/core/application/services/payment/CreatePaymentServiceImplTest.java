package br.com.fiap.controlepedidos.core.application.services.payment;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.impl.CreatePaymentServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentProviderNotAllowedException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutOrderException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreatePaymentServiceImplTest {

    private IPaymentRepository paymentRepository;
    private CreatePaymentServiceImpl createPaymentService;

    @BeforeEach
    void setup() {
        paymentRepository = mock(IPaymentRepository.class);
        createPaymentService = new CreatePaymentServiceImpl(paymentRepository);
    }

    @Test
    void createPayment_shouldSavePayment_whenValidPayment() {
        Order order = new Order();
        order.setId(java.util.UUID.randomUUID());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTotalCents(1000);
        payment.setProvider("Mercado Pago PIX QR CODE");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        Payment result = createPaymentService.createPayment(payment);

        assertThat(result).isNotNull();
        assertThat(result.getOrder()).isEqualTo(order);
        assertThat(result.getTotalCents()).isEqualTo(1000);
        assertThat(result.getProvider()).isEqualTo("Mercado Pago PIX QR CODE");

        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void createPayment_shouldThrowException_whenOrderIdIsNull() {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(null);

        Payment payment = new Payment();
        payment.setOrder(mockOrder);
        payment.setTotalCents(1000);
        payment.setProvider("Mercado Pago PIX QR CODE");

        assertThatThrownBy(() -> createPaymentService.createPayment(payment))
                .isInstanceOf(PaymentWithoutOrderException.class)
                .hasMessageContaining("Um pagamento deve estar vinculado a um pedido.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_shouldThrowException_whenTotalCentsIsZeroOrLess() {
        Order order = new Order();
        order.setId(java.util.UUID.randomUUID());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTotalCents(0);
        payment.setProvider("Mercado Pago PIX QR CODE");

        assertThatThrownBy(() -> createPaymentService.createPayment(payment))
                .isInstanceOf(PaymentWithoutValueException.class)
                .hasMessageContaining("O valor do pagamento não pode ser zero.");

        payment.setTotalCents(-100);

        assertThatThrownBy(() -> createPaymentService.createPayment(payment))
                .isInstanceOf(PaymentWithoutValueException.class)
                .hasMessageContaining("O valor do pagamento não pode ser zero.");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_shouldThrowException_whenProviderIsInvalid() {
        Order order = new Order();
        order.setId(java.util.UUID.randomUUID());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTotalCents(1000);
        payment.setProvider("Paypal");

        assertThatThrownBy(() -> createPaymentService.createPayment(payment))
                .isInstanceOf(PaymentProviderNotAllowedException.class)
                .hasMessageContaining("O provedor de pagamento deve ser 'MercadoPago'.");

        verify(paymentRepository, never()).save(any());
    }
}