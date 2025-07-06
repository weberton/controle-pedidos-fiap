package br.com.fiap.controlepedidos.core.application.services.checkout;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import br.com.fiap.controlepedidos.core.application.services.checkout.impl.StartCheckoutServiceImpl;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.application.services.payment.ICreatePaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCheckoutServiceTest {

    @Mock
    CreateOrderService createOrderService;

    @Mock
    IPaymentGateway paymentGateway;

    @Mock
    ICreatePaymentService createPaymentService;

    @InjectMocks
    StartCheckoutServiceImpl service;

    @Test
    void startCheckout_ShouldReturnPaymentToBePerformed() {
        // Arrange
        UUID cartId = UUID.randomUUID();

        Order fakeOrder = new Order();
        fakeOrder.setId(UUID.randomUUID());
        fakeOrder.setTotalCents(5000); // Exemplo de valor

        Payment paymentFromGateway = new Payment();
        paymentFromGateway.setId(UUID.randomUUID());
        paymentFromGateway.setPaymentStatus(PaymentStatus.WAITING);
        paymentFromGateway.setProvider("MercadoPago");
        paymentFromGateway.setQrCode("some-valid-qr-code");

        Payment finalPersistedPayment = new Payment();
        finalPersistedPayment.setId(UUID.randomUUID());
        finalPersistedPayment.setPaymentStatus(PaymentStatus.WAITING);
        finalPersistedPayment.setProvider("MercadoPago");
        finalPersistedPayment.setQrCode("some-valid-qr-code");
        finalPersistedPayment.setOrder(fakeOrder);
        finalPersistedPayment.setTotalCents(fakeOrder.getTotalCents());

        when(createOrderService.createOrder(cartId)).thenReturn(fakeOrder);
        when(paymentGateway.generatePixQrCodeMercadoPago(fakeOrder)).thenReturn(paymentFromGateway);
        when(createPaymentService.createPayment(paymentFromGateway)).thenReturn(finalPersistedPayment);

        // Act
        Payment result = service.startCheckout(cartId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrder()).isEqualTo(fakeOrder);
        assertThat(result.getTotalCents()).isEqualTo(fakeOrder.getTotalCents());
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.WAITING);
        assertThat(result.getProvider()).isEqualTo("MercadoPago");
        assertThat(result.getQrCode()).isEqualTo("some-valid-qr-code");

        verify(createOrderService).createOrder(cartId);
        verify(paymentGateway).generatePixQrCodeMercadoPago(fakeOrder);
        verify(createPaymentService).createPayment(paymentFromGateway);
    }
}
