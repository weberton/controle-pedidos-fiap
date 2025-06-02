package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.domain.entities.Order;

import java.util.UUID;

public interface CreateOrderService {

    Order createOrder(UUID cartId);

}
