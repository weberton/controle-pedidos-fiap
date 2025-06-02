package br.com.fiap.controlepedidos.core.application.services.checkout.impl;

import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import br.com.fiap.controlepedidos.core.application.services.checkout.StartCheckoutService;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartCheckoutServiceImpl implements StartCheckoutService {

    private final CreateOrderService createOrderService;
    private final IPaymentGateway paymentGateway;

    public StartCheckoutServiceImpl(CreateOrderService createOrderService, IPaymentGateway paymentGateway) {
        this.createOrderService = createOrderService;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public Payment startCheckout(UUID cartId) {

        //Creating Order and Payment to Request Payment Gateway
        Order newOrder = createOrderService.createOrder(cartId);
        Payment payment = paymentGateway.generatePixQrCodeMercadoPago(newOrder);

        //TODO realizar implementação de pagamento de payment no futuro
        payment.setOrder(newOrder);
        payment.setTotalCents(newOrder.getTotalCents());

        return payment;
    }
}