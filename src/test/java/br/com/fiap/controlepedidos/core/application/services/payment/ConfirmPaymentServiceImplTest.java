package br.com.fiap.controlepedidos.core.application.services.payment;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.impl.ConfirmPaymentServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import br.com.fiap.controlepedidos.core.domain.validations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ConfirmPaymentServiceImplTest {

    private IPaymentRepository paymentRepository;
    private ConfirmPaymentServiceImpl confirmPaymentService;

    private final UUID paymentId = UUID.randomUUID();
    private Payment dbPayment;
    private Payment inputPayment;

    @BeforeEach
    void setup() {
        paymentRepository = mock(IPaymentRepository.class);
        confirmPaymentService = new ConfirmPaymentServiceImpl(paymentRepository);

        dbPayment = new Payment();
        dbPayment.setId(paymentId);
        dbPayment.setPaymentStatus(PaymentStatus.WAITING);
        dbPayment.setTotalCents(1000);

        inputPayment = new Payment();
        inputPayment.setId(paymentId);
        inputPayment.setTotalCents(1000);
        inputPayment.setOrder(new Order());
        inputPayment.getOrder().setId(UUID.randomUUID());
    }

    @Test
    void confirmPayment_shouldConfirmSuccessfully() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = confirmPaymentService.confirmPayment(inputPayment);

        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        verify(paymentRepository, times(1)).save(result);
    }

    @Test
    void confirmPayment_shouldThrowWhenAlreadyPaid() {
        dbPayment.setPaymentStatus(PaymentStatus.PAID);
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(PaymentAlreadyConfirmedException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenCancelled() {
        dbPayment.setPaymentStatus(PaymentStatus.CANCELLED);
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(CancelledPaymentException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenStatusIsInvalid() {
        dbPayment.setPaymentStatus(null); // Status inválido
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(InvalidPaymentStatusException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenOrderIsNull() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));
        inputPayment.setOrder(null);

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(PaymentWithoutOrderException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenOrderIdIsNull() {
        UUID testPaymentId  = UUID.randomUUID();

        Order testOrder  = new Order();
        testOrder .setId(null);

        Payment testInputPayment  = new Payment();
        testInputPayment .setId(testPaymentId);
        testInputPayment .setOrder(testOrder );
        testInputPayment .setTotalCents(1000);

        Payment testDbPayment  = new Payment();
        testDbPayment .setId(testPaymentId);
        testDbPayment .setPaymentStatus(PaymentStatus.WAITING);
        testDbPayment .setTotalCents(1000);

        when(paymentRepository.findById(testPaymentId)).thenReturn(Optional.of(testDbPayment ));

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(testInputPayment))
                .isInstanceOf(PaymentWithoutOrderException.class)
                .hasMessageContaining("Um pagamento deve estar vinculado a um pedido.");
    }

    @Test
    void confirmPayment_shouldThrowWhenPaymentValueIsZero() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));
        inputPayment.setTotalCents(0);

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(PaymentWithoutValueException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenPaymentIsLessThanRequired() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(dbPayment));
        inputPayment.setTotalCents(500);

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(PaymentValueMismatchException.class);
    }

    @Test
    void confirmPayment_shouldThrowWhenPaymentNotFound() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> confirmPaymentService.confirmPayment(inputPayment))
                .isInstanceOf(RecordNotFoundException.class);
    }
}
