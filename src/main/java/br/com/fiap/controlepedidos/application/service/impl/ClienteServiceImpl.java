package br.com.fiap.controlepedidos.application.service.impl;

import br.com.fiap.controlepedidos.application.service.ClienteService;
import br.com.fiap.controlepedidos.common.RegistroExistenteException;
import br.com.fiap.controlepedidos.common.RegistroNaoEncontradoException;
import br.com.fiap.controlepedidos.domain.model.Cliente;
import br.com.fiap.controlepedidos.domain.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente criarCliente(final Cliente cliente) {
        // verificar se cpf já existe
        if (isCpfJaCadastrado(cliente.getCpf())) {
            throw new RegistroExistenteException("Cliente com CPF %s já possui cadastrado."
                    .formatted(cliente.getCpf()));
        }

        //verificar se email já existe
        if (isEmailJaCadastrado(cliente.getEmail())) {
            throw new RegistroExistenteException("Cliente com Email %s já possui cadastrado."
                    .formatted(cliente.getEmail()));
        }

        return this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorCpf(final String cpf) {
        return this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RegistroNaoEncontradoException("CPF %s não encontrado.".formatted(cpf)));
    }

    @Override
    public Page<Cliente> buscarTodos(final Pageable pageable) {
        return this.clienteRepository.findAll(pageable);
    }

    @Override
    public void apagar(UUID id) {
        this.clienteRepository.deleteById(id);
    }

    private boolean isCpfJaCadastrado(final String cpf) {
        return Objects.nonNull(cpf) && this.clienteRepository.findByCpf(cpf).isPresent();
    }

    private boolean isEmailJaCadastrado(final String email) {
        return this.clienteRepository.findByEmail(email).isPresent();
    }
}
