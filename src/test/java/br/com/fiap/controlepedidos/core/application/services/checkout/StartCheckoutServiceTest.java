package br.com.fiap.controlepedidos.core.application.services.checkout;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import br.com.fiap.controlepedidos.core.application.services.checkout.impl.StartCheckoutServiceImpl;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
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

    //Arrange
    @InjectMocks
    StartCheckoutServiceImpl service;

    @Test
    void startCheckout_ShouldReturnPaymentToBePerformed() {

        UUID cartId = UUID.randomUUID();

        Order fakeOrder = new Order();
        fakeOrder.setId(UUID.randomUUID());

        Payment fakePayment = new Payment();
        fakePayment.setId(UUID.randomUUID());
        fakePayment.setPaymentStatus(PaymentStatus.WAITING);
        fakePayment.setProvider("MercadoPago");
        fakePayment.setQrCode("some-valid-qr-code");

        when(createOrderService.createOrder(cartId)).thenReturn(fakeOrder);
        when(paymentGateway.generatePixQrCodeMercadoPago(fakeOrder)).thenReturn(fakePayment);


        Payment result = service.startCheckout(cartId);

        assertThat(result).isNotNull();
        assertThat(result.getOrder().getId()).isEqualTo(fakeOrder.getId());
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.WAITING);
        assertThat(result.getProvider()).isEqualTo("MercadoPago");
        assertThat(result.getQrCode()).isEqualTo("some-valid-qr-code");

        verify(createOrderService).createOrder(cartId);
        verify(paymentGateway).generatePixQrCodeMercadoPago(fakeOrder);
    }
}