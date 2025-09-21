package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.ports.IAzureAPIMGateway;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.CreateCustomerServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Account;
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
class CreateCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private IAzureAPIMGateway cpfRepository;

    @InjectMocks
    private CreateCustomerServiceImpl createCustomer;

    @Test
    void criarCliente_quandoEmailAndCpfNaoExiste_criarCliente() {
        Customer cliente = Customer.builder().cpf("123").email("myEmail@gmail.com").name("Nome").accountid("").build();

        Account account = Account.builder().userId("user-123").build();

        when(customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());


        when(cpfRepository.createCustomerAccountByCPF(any(Customer.class))).thenReturn(account);

        createCustomer.createCustomer(cliente);

        // garante que salvou com o mesmo cliente atualizado
        verify(customerRepository).save(cliente);
        assertThat(cliente.getAccountid()).isEqualTo("user-123");
    }

    @Test
    void criarCliente_quandoEmailJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder().cpf("123").email("myEmail@gmail.com").name("Nome").accountid("").build();

        when(customerRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        ExistentRecordException exception = assertThrows(ExistentRecordException.class, () -> createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com Email %s já possui cadastrado.".formatted(cliente.getEmail()));

        verify(customerRepository, never()).save(any());
        verifyNoInteractions(cpfRepository);
    }

    @Test
    void criarCliente_quandoCpfJaExiste_lancaExcecao() {
        Customer cliente = Customer.builder().cpf("123").email("myEmail@gmail.com").name("Nome").accountid("").build();

        when(customerRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        ExistentRecordException exception = assertThrows(ExistentRecordException.class, () -> createCustomer.createCustomer(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com CPF %s já possui cadastrado.".formatted(cliente.getCpf()));

        verify(customerRepository, never()).save(any());
        verifyNoInteractions(cpfRepository);
    }
}