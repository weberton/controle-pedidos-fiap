package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;

import java.util.UUID;

public interface PayOrderService {

    Payment payOrder(Order order, int receivedValue) throws Exception;

}
