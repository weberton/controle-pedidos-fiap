package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.IFindCustomerByCPF;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindCustomerByCPFImpl implements IFindCustomerByCPF {

    private final ICustomerRepository customerRepository;


    public FindCustomerByCPFImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByCPF(String cpf) {
        return this.customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecordNotFoundException("CPF %s não encontrado.".formatted(cpf)));
    }
}
