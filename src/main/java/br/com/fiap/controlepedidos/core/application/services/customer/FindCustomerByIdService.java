package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.domain.entities.Customer;

import java.util.UUID;

public interface FindCustomerByIdService {
    Customer findById(UUID id);
}
