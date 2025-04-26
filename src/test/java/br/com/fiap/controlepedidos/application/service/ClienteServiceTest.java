package br.com.fiap.controlepedidos.application.service;

import br.com.fiap.controlepedidos.application.service.impl.ClienteServiceImpl;
import br.com.fiap.controlepedidos.common.RegistroExistenteException;
import br.com.fiap.controlepedidos.common.RegistroNaoEncontradoException;
import br.com.fiap.controlepedidos.domain.model.Cliente;
import br.com.fiap.controlepedidos.domain.repository.ClienteRepository;
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
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void criarCliente_quandoEmailAndCpfNaoExiste_criarCliente() {
        Cliente cliente = Cliente.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .nome("Nome")
                .build();
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());

        clienteService.criarCliente(cliente);

        verify(clienteRepository).save(cliente);
    }

    @Test
    void criarCliente_quandoEmailJaExiste_lancaExcecao() {
        Cliente cliente = Cliente.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .nome("Nome")
                .build();
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));
        RegistroExistenteException exception = assertThrows(RegistroExistenteException.class,
                () -> clienteService.criarCliente(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com Email %s já possui cadastrado."
                .formatted(cliente.getEmail()));

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void criarCliente_quandoCpfJaExiste_lancaExcecao() {
        Cliente cliente = Cliente.builder()
                .cpf("123")
                .email("myEmail@gmail.com")
                .nome("Nome")
                .build();

        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));
        RegistroExistenteException exception = assertThrows(RegistroExistenteException.class,
                () -> clienteService.criarCliente(cliente));

        assertThat(exception.getMessage()).isEqualTo("Cliente com CPF %s já possui cadastrado."
                .formatted(cliente.getCpf()));

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void buscarPorCpf_quandoCpfNaoEncontrado_lancarExcecao() {
        var cpf = "123";
        when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        RegistroNaoEncontradoException exception = assertThrows(RegistroNaoEncontradoException.class,
                () -> clienteService.buscarPorCpf(cpf));
        assertThat(exception.getMessage()).isEqualTo("CPF %s não encontrado.".formatted(cpf));
    }
}
