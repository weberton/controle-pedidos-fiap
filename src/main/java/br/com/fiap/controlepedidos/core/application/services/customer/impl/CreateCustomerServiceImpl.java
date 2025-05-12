package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.CreateCustomerService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateCustomerServiceImpl implements CreateCustomerService {

    private final CustomerRepository customerRepository;

    public CreateCustomerServiceImpl(CustomerRepository customerRepository) {
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
