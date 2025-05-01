package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.IDeleteCustomerById;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCustomerByIdImpl implements IDeleteCustomerById {

    private final ICustomerRepository _customerRepository;

    public DeleteCustomerByIdImpl(ICustomerRepository customerRepository) {
       _customerRepository = customerRepository;
    }

    @Override
    public void delete(UUID id) {
        this._customerRepository.deleteById(id);
    }
}
