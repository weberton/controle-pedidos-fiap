package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.application.services.order.GetOrderByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class GetOrderByIdServiceImpl implements GetOrderByIdService {

    private final IOrderRepository orderRepository;

    public GetOrderByIdServiceImpl(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public Order getById(UUID orderId) throws Exception {
        return null;
    }
}
