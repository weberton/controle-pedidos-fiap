package br.com.fiap.controlepedidos.core.application.services.checkout.impl;

import br.com.fiap.controlepedidos.core.application.services.checkout.ConfirmQRCodePaymentOrder;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.PayOrderService;
import br.com.fiap.controlepedidos.core.application.services.payment.ICancelPaymentService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmQRCodePaymentOrderImpl implements ConfirmQRCodePaymentOrder {

    private final ICancelPaymentService cancelPaymentService;
    private final FindOrderByIdService findOrderByIdService;
    private final PayOrderService payOrderService;

    public ConfirmQRCodePaymentOrderImpl(ICancelPaymentService cancelPaymentService, FindOrderByIdService findOrderByIdService, PayOrderService payOrderService) {
        this.cancelPaymentService = cancelPaymentService;
        this.findOrderByIdService = findOrderByIdService;
        this.payOrderService = payOrderService;
    }

    @Override
    public Payment confirmQrCodePayment(UUID orderId, int paidValue) {

        Order orderToPay = findOrderByIdService.getById(orderId);
        Payment result = payOrderService.payOrder(orderToPay, paidValue);

        if (result.getPaymentStatus().equals(PaymentStatus.PAID)) {
            return result;
        } else {
            return cancelPaymentService.cancelPayment(result);
        }
    }
}