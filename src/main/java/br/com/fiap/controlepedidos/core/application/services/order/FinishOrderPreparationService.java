package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.domain.entities.Order;

import java.util.UUID;

public interface FinishOrderPreparationService {
    Order perform(UUID orderId) throws Exception;
}