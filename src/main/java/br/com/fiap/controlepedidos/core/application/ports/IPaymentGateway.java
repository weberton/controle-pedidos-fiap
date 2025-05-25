package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentResponseDTO;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;

public interface IPaymentGateway {
    public Payment generatePixQrCodeMercadoPago(Order order);
}