package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.PayOrderService;
import br.com.fiap.controlepedidos.core.application.services.payment.IConfirmPaymentService;
import br.com.fiap.controlepedidos.core.application.services.payment.IFindPaymentByOrderId;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import org.springframework.stereotype.Service;


@Service
public class PayOrderServiceImpl implements PayOrderService {

    private final IOrderRepository orderRepository;

    private final IConfirmPaymentService confirmPaymentService;
    private final FindCustomerByIdService findCustomerByIdService;

    private final IFindPaymentByOrderId findPaymentByOrderId;

    public PayOrderServiceImpl(IOrderRepository orderRepository, IConfirmPaymentService confirmPaymentService, FindCustomerByIdService findCustomerByIdService, IFindPaymentByOrderId findPaymentByOrderId) {
        this.orderRepository = orderRepository;
        this.confirmPaymentService = confirmPaymentService;
        this.findCustomerByIdService = findCustomerByIdService;
        this.findPaymentByOrderId = findPaymentByOrderId;
    }

    @Override
    public Payment payOrder(Order order, int receivedValue) {
        Payment payment = findPaymentByOrderId.findPayment(order.getId());

        boolean isOrderPaid = order.payOrder(receivedValue);

        if (isOrderPaid) {
            payment.setTotalCents(receivedValue);
            payment = confirmPaymentService.confirmPayment(payment);
        }

        if (payment.getPaymentStatus().equals(PaymentStatus.PAID) && isOrderPaid) {
            orderRepository.save(order);

            if (order.getCustomer() != null) {
                order.setCustomer(
                        findCustomerByIdService.findById(order.getCustomer().getId())
                );
            } else {
                order.setCustomer(null);
            }
        }
        return payment;
    }
}