package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.PayOrderService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.stereotype.Service;


@Service
public class PayOrderServiceImpl implements PayOrderService {

    private final IOrderRepository orderRepository;
    private final FindCustomerByIdService findCustomerByIdService;

    public PayOrderServiceImpl(IOrderRepository orderRepository, FindCustomerByIdService findCustomerByIdService) {
        this.orderRepository = orderRepository;
        this.findCustomerByIdService = findCustomerByIdService;
    }

    @Override
    public Payment payOrder(Order order, float receivedValue) throws Exception {

        boolean result = order.payOrder(receivedValue);


        if (result) {
            orderRepository.save(order);
            order.setCustomer(findCustomerByIdService.findById(order.getCustomer().getId()));
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.receivePayment();

            return payment;
        } else {
            return new Payment();
        }
    }
}
