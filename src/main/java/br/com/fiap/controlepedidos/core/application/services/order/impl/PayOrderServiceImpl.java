package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.application.services.order.PayOrderByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class PayOrderByIdServiceImpl implements PayOrderByIdService {

    private final IOrderRepository orderRepository;

    public PayOrderByIdServiceImpl(FindCartService findCartService, IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order payOrder(UUID orderId) throws Exception {
        return null;
    }
}
