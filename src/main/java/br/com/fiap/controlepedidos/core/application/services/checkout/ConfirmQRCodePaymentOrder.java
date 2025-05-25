package br.com.fiap.controlepedidos.core.application.services.checkout;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

import java.util.UUID;

public interface ConfirmQRCodePaymentOrder {
    public Payment confirmQrCodePayment(UUID orderId, float paidValue) throws Exception;
}
