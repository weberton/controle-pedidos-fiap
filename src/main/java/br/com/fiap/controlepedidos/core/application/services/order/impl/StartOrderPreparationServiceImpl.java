package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.StartOrderPreparation;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartOrderPreparationServiceImpl implements StartOrderPreparation {

    private final FindOrderByIdService findOrderByIdService;
    private final FindCustomerByIdService findCustomerByIdService;
    private final IOrderRepository orderRepository;

    public StartOrderPreparationServiceImpl(FindOrderByIdService findOrderByIdService, FindCustomerByIdService findCustomerByIdService, IOrderRepository orderRepository) {
        this.findOrderByIdService = findOrderByIdService;
        this.findCustomerByIdService = findCustomerByIdService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order perform(UUID orderId) throws Exception {
        Order order = findOrderByIdService.getById(orderId);
        order.startOrderPreparation();
        orderRepository.save(order);
        if(order.getCustomer() != null) {
            Customer customer = findCustomerByIdService.findById(order.getCustomer().getId());
            order.setCustomer(customer);
        }
        return order;
    }
}