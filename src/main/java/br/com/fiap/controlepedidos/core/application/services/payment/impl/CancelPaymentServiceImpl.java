package br.com.fiap.controlepedidos.core.application.services.payment.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.ICancelPaymentService;
import br.com.fiap.controlepedidos.core.application.services.payment.ICreatePaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentProviderNotAllowedException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutOrderException;
import br.com.fiap.controlepedidos.core.domain.validations.PaymentWithoutValueException;
import org.springframework.stereotype.Service;

@Service
public class CancelPaymentServiceImpl implements ICancelPaymentService {

    private final IPaymentRepository paymentRepository;

    public CancelPaymentServiceImpl(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment cancelPayment(Payment payment) {
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            throw new PaymentWithoutOrderException("Um pagamento deve estar vinculado a um pedido.");
        }
        payment.cancelPayment();
        return this.paymentRepository.save(payment);
    }
}
