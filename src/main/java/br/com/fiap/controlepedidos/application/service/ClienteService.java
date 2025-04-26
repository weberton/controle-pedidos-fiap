package br.com.fiap.controlepedidos.application.service;

import br.com.fiap.controlepedidos.domain.model.Cliente;

public interface ClienteService {
    Cliente criarCliente(Cliente cliente);

    Cliente buscarPorCpf(String cpf);
}
