package br.com.fiap.controlepedidos.core.application.services.payment;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

public interface ICreatePaymentService {
    Payment createPayment(Payment payment);
}
