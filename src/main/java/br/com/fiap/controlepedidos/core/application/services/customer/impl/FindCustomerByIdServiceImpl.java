package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindCustomerByIdServiceImpl implements FindCustomerByIdService {

    private final CustomerRepository customerRepository;

    public FindCustomerByIdServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findById(UUID id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Cliente não encontrado."));
    }
}
