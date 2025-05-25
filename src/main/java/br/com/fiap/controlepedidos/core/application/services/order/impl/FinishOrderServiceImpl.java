package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.FinishOrderPreparationService;
import br.com.fiap.controlepedidos.core.application.services.order.FinishOrderService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FinishOrderServiceImpl implements FinishOrderService {

    private final FindOrderByIdService findOrderByIdService;
    private final IOrderRepository orderRepository;
    private final FindCustomerByIdService findCustomerByIdService;

    public FinishOrderServiceImpl(FindOrderByIdService findOrderByIdService, IOrderRepository orderRepository, FindCustomerByIdService findCustomerByIdService) {
        this.findOrderByIdService = findOrderByIdService;
        this.orderRepository = orderRepository;
        this.findCustomerByIdService = findCustomerByIdService;
    }

    @Override
    public Order perform(UUID orderId) throws Exception {
        Order order = findOrderByIdService.getById(orderId);
        order.finishOrder();
        orderRepository.save(order);
        Customer customer = findCustomerByIdService.findById(order.getCustomer().getId());
        order.setCustomer(customer);
        return order;
    }
}