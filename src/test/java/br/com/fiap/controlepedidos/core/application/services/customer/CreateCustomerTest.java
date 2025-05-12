package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.CreateCustomerServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CreateCustomerServiceImpl createCustomer;

    @Test
    void criarCliente_quandoEmailAndCpfNaoExiste_criarCliente() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();
        when(customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());

        createCustomer.createCustomer(cliente);

        verify(customerRepository).save(cliente);
    }

    @Test
    void criarCliente_quandoEmailJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();
        when(customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));
        ExistentRecordException exception = assertThrows(ExistentRecordException.class,
                () -> createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com Email %s já possui cadastrado."
                .formatted(cliente.getEmail()));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void criarCliente_quandoCpfJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();

        when(customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));
        ExistentRecordException exception = assertThrows(ExistentRecordException.class,
                () -> createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com CPF %s já possui cadastrado."
                .formatted(cliente.getCpf()));

        verify(customerRepository, never()).save(any());
    }
}
