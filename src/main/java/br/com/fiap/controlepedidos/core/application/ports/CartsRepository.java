package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartsRepository extends JpaRepository<Cart, UUID> {
}
