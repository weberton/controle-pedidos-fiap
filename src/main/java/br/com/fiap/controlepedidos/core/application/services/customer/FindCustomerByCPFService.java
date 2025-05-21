package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.domain.entities.Customer;

public interface FindCustomerByCPFService {
    Customer findByCPF (String cpf);

}
