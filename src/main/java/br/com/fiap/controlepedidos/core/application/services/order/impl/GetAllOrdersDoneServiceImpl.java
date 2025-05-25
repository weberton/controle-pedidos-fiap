package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.order.GetAllOrdersDoneService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetAllOrdersDoneServiceImpl implements GetAllOrdersDoneService {

    private final IOrderRepository orderRepository;

    public GetAllOrdersDoneServiceImpl(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> getAll(Pageable pageable) throws Exception {
        return orderRepository.findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.DONE, pageable);
    }
}
