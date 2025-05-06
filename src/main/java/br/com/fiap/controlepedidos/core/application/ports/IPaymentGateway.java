package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentResponseDTO;

public interface IPaymentGateway {
    public MercadoPagoPaymentResponseDTO generatePixQrCodeMercadoPago(MercadoPagoPaymentRequestDTO request);
}