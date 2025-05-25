package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllOrdersReadyService {
    Page<Order> getAll(Pageable pageable) throws Exception;
}
