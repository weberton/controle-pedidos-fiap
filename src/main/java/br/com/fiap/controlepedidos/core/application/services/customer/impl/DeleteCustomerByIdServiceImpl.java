package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.DeleteCustomerByIdService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCustomerByIdServiceImpl implements DeleteCustomerByIdService {

    private final CustomerRepository customerRepository;


    public DeleteCustomerByIdServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void delete(UUID id) {
        this.customerRepository.deleteById(id);
    }
}
