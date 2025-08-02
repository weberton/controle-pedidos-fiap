package br.com.fiap.controlepedidos.core.application.services.checkout.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import br.com.fiap.controlepedidos.core.application.services.checkout.StartCheckoutService;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.application.services.payment.ICreatePaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartCheckoutServiceImpl implements StartCheckoutService {

    private final ICreatePaymentService createPaymentService;
    private final CreateOrderService createOrderService;
    private final IPaymentGateway paymentGateway;

    public StartCheckoutServiceImpl(ICreatePaymentService createPaymentService, CreateOrderService createOrderService, IPaymentGateway paymentGateway) {
        this.createPaymentService = createPaymentService;
        this.createOrderService = createOrderService;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Payment startCheckout(UUID cartId) {

        Order newOrder = createOrderService.createOrder(cartId);
        Payment payment = paymentGateway.generatePixQrCodeMercadoPago(newOrder);
        payment.setOrder(newOrder);
        payment.setTotalCents(newOrder.getTotalCents());

        return createPaymentService.createPayment(payment);

    }
}