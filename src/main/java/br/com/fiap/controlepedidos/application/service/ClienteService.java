package br.com.fiap.controlepedidos.application.service;

import br.com.fiap.controlepedidos.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClienteService {
    Cliente criarCliente(Cliente cliente);

    Cliente buscarPorCpf(String cpf);

    Page<Cliente> buscarTodos(Pageable pageable);

    void apagar(UUID id);
}
