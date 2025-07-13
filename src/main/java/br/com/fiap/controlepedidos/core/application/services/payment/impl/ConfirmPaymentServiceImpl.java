package br.com.fiap.controlepedidos.core.application.services.payment.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.IConfirmPaymentService;
import br.com.fiap.controlepedidos.core.application.services.payment.ICreatePaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import br.com.fiap.controlepedidos.core.domain.validations.*;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
public class ConfirmPaymentServiceImpl implements IConfirmPaymentService {

    private final IPaymentRepository paymentRepository;

    public ConfirmPaymentServiceImpl(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment confirmPayment(Payment payment) {
        validatePayment(payment);
        return makePayment(payment);
    }

    private Payment makePayment(Payment payment) {
        payment.receivePayment();
        return this.paymentRepository.save(payment);
    }

    private void validatePayment(Payment payment) {

        Payment dbPayment = this.paymentRepository.findById(payment.getId())
                .orElseThrow(() -> new RecordNotFoundException("Pagamento não encontrado."));

        if (dbPayment.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new PaymentAlreadyConfirmedException("Pagamento já realizado anteriormente.");
        }

        if (dbPayment.getPaymentStatus().equals(PaymentStatus.CANCELLED)) {
            throw new CancelledPaymentException("Pagamento já cancelado anteriormente.");
        }

        if (!EnumSet.allOf(PaymentStatus.class).contains(dbPayment.getPaymentStatus())) {
            throw new InvalidPaymentStatusException("Status de pagamento inválido.");
        }

        if (payment.getOrder().getId() == null) {
            throw new PaymentWithoutOrderException("Um pagamento deve estar vinculado a um pedido.");
        }

        if (payment.getTotalCents() <= 0) {
            throw new PaymentWithoutValueException("O valor do pagamento não pode ser zero.");
        }

        if (payment.getTotalCents() < dbPayment.getTotalCents()) {
            throw new PaymentValueMismatchException("O valor do pagamento não pode ser menor que o valor do pedido.");
        }
    }
}