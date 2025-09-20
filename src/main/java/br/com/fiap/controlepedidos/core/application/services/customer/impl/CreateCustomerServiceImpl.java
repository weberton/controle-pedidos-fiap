package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.ports.IAzureAPIMGateway;
import br.com.fiap.controlepedidos.core.application.services.customer.CreateCustomerService;
import br.com.fiap.controlepedidos.core.domain.entities.Account;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateCustomerServiceImpl implements CreateCustomerService {

    private final CustomerRepository customerRepository;
    private final IAzureAPIMGateway cpfRepository;

    public CreateCustomerServiceImpl(CustomerRepository customerRepository, IAzureAPIMGateway cpfAuthenticationAccountRepository) {
        this.customerRepository = customerRepository;
        this.cpfRepository = cpfAuthenticationAccountRepository;
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

        if (isCpfValid(customer.getCpf())) {
            Account account = cpfRepository.createCustomerAccountByCPF(customer);
            updateCustomerWithAccount(customer, account);
        }

        return this.customerRepository.save(customer);
    }

    private void updateCustomerWithAccount(Customer customer, Account account) {
        if (Objects.nonNull(account) && Objects.nonNull(account.getUserId())) {
            customer.setAccountid(account.getUserId());
        }
    }

    private boolean isCpfExists(final String cpf) {
        return Objects.nonNull(cpf) && this.customerRepository.findByCpf(cpf).isPresent();
    }

    private boolean isEmailExists(final String email) {
        return this.customerRepository.findByEmail(email).isPresent();
    }

    private boolean isCpfValid(final String cpf) {
        return Objects.nonNull(cpf) && !cpf.isEmpty();
    }
}
