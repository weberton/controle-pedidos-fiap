package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.IDeleteCustomerById;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCustomerByIdImpl implements IDeleteCustomerById {

    private final ICustomerRepository customerRepository;

    public DeleteCustomerByIdImpl(ICustomerRepository customerRepository) {
       this.customerRepository = customerRepository;
    }

    @Override
    public void delete(UUID id) {
        this.customerRepository.deleteById(id);
    }
}
