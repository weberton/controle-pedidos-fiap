package br.com.fiap.controlepedidos.application.service.impl;

import br.com.fiap.controlepedidos.application.service.ClienteService;
import br.com.fiap.controlepedidos.common.RegistroExistenteException;
import br.com.fiap.controlepedidos.common.RegistroNaoEncontradoException;
import br.com.fiap.controlepedidos.domain.model.Cliente;
import br.com.fiap.controlepedidos.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente criarCliente(Cliente cliente) {
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
    public Cliente buscarPorCpf(String cpf) {
        return this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RegistroNaoEncontradoException("CPF %s não encontrado.".formatted(cpf)));
    }

    private boolean isCpfJaCadastrado(final String cpf) {
        return this.clienteRepository.findByCpf(cpf).isPresent();
    }

    private boolean isEmailJaCadastrado(final String email) {
        return this.clienteRepository.findByEmail(email).isPresent();
    }
}
