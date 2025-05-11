package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.domain.entities.Customer;

public interface IFindCustomerByCPF {
    Customer findByCPF (String cpf);

}
