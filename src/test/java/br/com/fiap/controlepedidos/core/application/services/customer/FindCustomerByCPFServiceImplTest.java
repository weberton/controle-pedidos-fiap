package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.FindCustomerByCPFServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCustomerByCPFServiceImplTest {
    @Mock
    private ICustomerRepository customerRepository;
    @InjectMocks
    private FindCustomerByCPFServiceImpl findCustomerByCPF;

    @Test
    void findByCPF_whenCpfIsFound_returnsCustomer() {
        var id = UUID.randomUUID();
        var cpf = "34550838035";
        var name = "Name";
        var customerFromDb = Optional.of(Customer.builder().id(id).cpf(cpf).name(name).build());

        when(customerRepository.findByCpf(cpf)).thenReturn(customerFromDb);

        Customer result = findCustomerByCPF.findByCPF(cpf);

        verify(customerRepository).findByCpf(cpf);
        assertThat(result).isEqualTo(customerFromDb.get());
    }

    @Test
    void findByCPF_whenCpfIsNotFound_throwsException() {
        var cpf = "34550838035";

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> findCustomerByCPF.findByCPF(cpf));

        assertThat(exception.getMessage()).isEqualTo(("CPF %s não encontrado.".formatted(cpf)));
    }
}
