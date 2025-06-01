package br.com.fiap.controlepedidos.core.application.services.checkout;

import br.com.fiap.controlepedidos.core.application.services.checkout.impl.ConfirmQRCodePaymentOrderImpl;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.PayOrderService;
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
class ConfirmQRCodePaymentOrderTest {

    @Mock
    private FindOrderByIdService findOrderByIdService;

    @Mock
    private PayOrderService payOrderService;

    @InjectMocks
    private ConfirmQRCodePaymentOrderImpl service;

    @Test
    void confirmPayment_ShouldReturnPaymentWithStatusPaid() {

        UUID orderId = UUID.randomUUID();
        Order fakeOrderToPay = new Order();
        fakeOrderToPay.setId(orderId);
        fakeOrderToPay.setTotalCents(2250);

        Payment fakePayment = new Payment();
        fakePayment.setId(UUID.randomUUID());
        fakePayment.setPaymentStatus(PaymentStatus.PAID);
        fakePayment.setProvider("MercadoPago");
        fakePayment.setQrCode("some-valid-qr-code");

        when(findOrderByIdService.getById(orderId)).thenReturn(fakeOrderToPay);
        when(payOrderService.payOrder(fakeOrderToPay, 2250)).thenReturn(fakePayment);


        Payment result = service.confirmQrCodePayment(orderId, 2250);

        assertThat(result).isNotNull();
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(result.getProvider()).isEqualTo("MercadoPago");
        assertThat(result.getQrCode()).isEqualTo("some-valid-qr-code");

        verify(findOrderByIdService).getById(orderId);
        verify(payOrderService).payOrder(fakeOrderToPay, 2250);
    }
}
