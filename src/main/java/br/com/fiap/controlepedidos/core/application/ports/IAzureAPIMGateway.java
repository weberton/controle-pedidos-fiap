package br.com.fiap.controlepedidos.core.application.ports;

import br.com.fiap.controlepedidos.core.domain.entities.Account;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;

public interface IAzureAPIMGateway {
    public Account createCustomerAccountByCPF(Customer customer);

    public boolean authenticateCustomerAccountByCPF(Customer customer);
}