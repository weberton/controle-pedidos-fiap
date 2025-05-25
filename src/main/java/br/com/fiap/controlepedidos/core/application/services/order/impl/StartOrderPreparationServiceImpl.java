package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.StartOrderPreparation;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StartOrderPreparationImpl implements StartOrderPreparation {

    private final FindOrderByIdService findOrderByIdService;
    private final IOrderRepository orderRepository;

    public StartOrderPreparationImpl(FindOrderByIdService findOrderByIdService, IOrderRepository orderRepository) {
        this.findOrderByIdService = findOrderByIdService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order perform(UUID orderId) throws Exception {
        Order order = findOrderByIdService.getById(orderId);
        order.prepareOrder();
        orderRepository.save(order);
        return order;
    }
}