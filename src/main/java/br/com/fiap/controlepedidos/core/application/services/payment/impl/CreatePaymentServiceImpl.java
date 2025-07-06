package br.com.fiap.controlepedidos.core.application.services.payment.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.ICreatePaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentProviderNotAllowedException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutOrderException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutValueException;
import org.springframework.stereotype.Service;

@Service
public class CreatePaymentServiceImpl implements ICreatePaymentService {

    private final IPaymentRepository paymentRepository;

    public CreatePaymentServiceImpl(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {

        if (payment.getOrder().getId() == null) {
            throw new PaymentWithoutOrderException("Um pagamento deve estar vinculado a um pedido.");
        }

        if (payment.getTotalCents() <= 0) {
            throw new PaymentWithoutValueException("O valor do pagamento não pode ser zero.");
        }

        if (!payment.getProvider().equals("Mercado Pago PIX QR CODE")) {
            throw new PaymentProviderNotAllowedException("O provedor de pagamento deve ser 'MercadoPago'.");
        }

        return this.paymentRepository.save(payment);
    }
}
