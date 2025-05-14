package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByCPFService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class FindCustomerByCPFServiceImpl implements FindCustomerByCPFService {

    private final CustomerRepository customerRepository;

    public FindCustomerByCPFServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByCPF(String cpf) {
        return this.customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecordNotFoundException("CPF %s não encontrado.".formatted(cpf)));
    }
}
