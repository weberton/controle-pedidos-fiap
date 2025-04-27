package br.com.fiap.controlepedidos.core.application.service;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.ICreateCustomer;
import br.com.fiap.controlepedidos.core.application.services.customer.IFindCustomerByCPF;
import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
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
class ClienteServiceTest {
    @Mock
    private ICustomerRepository _customerRepository;
    @InjectMocks
    private final ICreateCustomer _createCustomer;
    private final IFindCustomerByCPF _findCustomerByCPF;

    public ClienteServiceTest(ICreateCustomer _createCustomer, IFindCustomerByCPF findCustomerByCPF) {
        this._createCustomer = _createCustomer;
        this._findCustomerByCPF = findCustomerByCPF;
    }

    @Test
    void criarCliente_quandoEmailAndCpfNaoExiste_criarCliente() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();
        when(_customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(_customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());

        _createCustomer.createCustomer(cliente);

        verify(_customerRepository).save(cliente);
    }

    @Test
    void criarCliente_quandoEmailJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();
        when(_customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));
        ExistentRecordException exception = assertThrows(ExistentRecordException.class,
                () -> _createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com Email %s já possui cadastrado."
                .formatted(cliente.getEmail()));

        verify(_customerRepository, never()).save(any());
    }

    @Test
    void criarCliente_quandoCpfJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .name("Nome")
                .build();

        when(_customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));
        ExistentRecordException exception = assertThrows(ExistentRecordException.class,
                () -> _createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com CPF %s já possui cadastrado."
                .formatted(cliente.getCpf()));

        verify(_customerRepository, never()).save(any());
    }

    @Test
    void buscarPorCpf_quandoCpfNaoEncontrado_lancarExcecao() {
        var cpf = "123";
        when(_customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> _findCustomerByCPF.findByCPF(cpf));
        assertThat(exception.getMessage()).isEqualTo("CPF %s não encontrado.".formatted(cpf));
    }
}
