package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
