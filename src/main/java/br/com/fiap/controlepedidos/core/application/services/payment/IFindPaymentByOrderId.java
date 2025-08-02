package br.com.fiap.controlepedidos.core.application.services.payment;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

import java.util.UUID;

public interface IFindPaymentByOrderId {
    Payment findPayment(UUID orderId);
}
