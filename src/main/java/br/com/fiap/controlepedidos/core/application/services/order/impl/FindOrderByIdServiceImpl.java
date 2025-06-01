package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.order.FindOrderByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class FindOrderByIdServiceImpl implements FindOrderByIdService {

    private final IOrderRepository orderRepository;

    public FindOrderByIdServiceImpl(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getById(UUID orderId) throws Exception {
        try {
            Optional<Order> result = orderRepository.findById(orderId);

            if (result.isPresent()) {
                return result.get();
            } else {
                throw new RecordNotFoundException("Pedido com ID %s não encontrado.".formatted(orderId));
            }
        } catch (Exception e) {
            e.printStackTrace(); // ou loga com SLF4J
            throw e; // pra propagar
        }
    }
}
