package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByOrderStatusOrderByUpdatedAtAsc(OrderStatus status, Pageable pageable);

    Page<Order> findByOrderStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {
            "cart",
            "cart.items",
            "cart.items.product"
    })
    Page<Order> findAll(Pageable pageable);


}