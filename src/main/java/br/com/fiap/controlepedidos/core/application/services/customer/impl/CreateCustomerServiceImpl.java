package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.ICreateCustomer;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateCustomerServiceImpl implements ICreateCustomer {

    private final ICustomerRepository customerRepository;

    public CreateCustomerServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {

        if (isCpfExists(customer.getCpf())) {
            throw new ExistentRecordException("Cliente com CPF %s já possui cadastrado."
                    .formatted(customer.getCpf()));
        }

        if (isEmailExists(customer.getEmail())) {
            throw new ExistentRecordException("Cliente com Email %s já possui cadastrado."
                    .formatted(customer.getEmail()));
        }

        return this.customerRepository.save(customer);
    }

    private boolean isCpfExists(final String cpf) {
        return Objects.nonNull(cpf) && this.customerRepository.findByCpf(cpf).isPresent();
    }

    private boolean isEmailExists(final String email) {
        return this.customerRepository.findByEmail(email).isPresent();
    }
}
