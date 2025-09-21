package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.APIMClient;
import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByCPFService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class FindCustomerByCPFServiceImpl implements FindCustomerByCPFService {

    private final CustomerRepository customerRepository;
    private final APIMClient apimClient;

    public FindCustomerByCPFServiceImpl(CustomerRepository customerRepository, APIMClient apimClient) {
        this.customerRepository = customerRepository;
        this.apimClient = apimClient;
    }

    @Override
    public Customer findByCPF(String cpf) {

        Customer foundCustomer = this.customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecordNotFoundException("CPF %s não encontrado.".formatted(cpf)));

        if (!foundCustomer.getCpf().isEmpty() && !foundCustomer.getAccountid().isEmpty()) {
            boolean isValid = apimClient.authenticateCustomerAccountByCPF(foundCustomer);

            if (!isValid) {
                throw new RecordNotFoundException("CPF %s não encontrado na base de autenticação.".formatted(cpf));
            }
        }

        return foundCustomer;
    }
}
