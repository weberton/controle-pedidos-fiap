package br.com.fiap.controlepedidos.core.application.services.payment.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentRepository;
import br.com.fiap.controlepedidos.core.application.services.payment.IFindPaymentByOrderId;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindPaymentByOrderIdServiceImpl implements IFindPaymentByOrderId {
    private final IPaymentRepository paymentRepository;

    public FindPaymentByOrderIdServiceImpl(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findPayment(UUID orderId) {

        Payment payment = new Payment();

        if (orderId != null) {
            payment = paymentRepository.findWithOrderByOrderId(orderId);
        }

        return payment;
    }
}