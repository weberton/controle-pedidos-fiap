package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPaymentRepository extends JpaRepository<Payment, UUID> {
    @EntityGraph(attributePaths = {"order"})
    Payment findWithOrderByOrderId(UUID orderId);
}
