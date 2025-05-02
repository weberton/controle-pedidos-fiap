package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.IFindAllCustomers;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindAllCustomersImpl implements IFindAllCustomers {

    private final ICustomerRepository customerRepository;

    public FindAllCustomersImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return this.customerRepository.findAll(pageable);
    }
}
