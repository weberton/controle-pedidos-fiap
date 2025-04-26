package br.com.fiap.controlepedidos.adapter.rest;

import br.com.fiap.controlepedidos.adapter.rest.contract.ClienteApi;
import br.com.fiap.controlepedidos.adapter.rest.dto.ClienteDTO;
import br.com.fiap.controlepedidos.application.service.ClienteService;
import br.com.fiap.controlepedidos.domain.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteController implements ClienteApi {

    private final ClienteService clienteService;

    public ClienteController(final ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public ResponseEntity<ClienteDTO> criar(final ClienteDTO clienteDTO) {
        Cliente clienteSalvo = this.clienteService.criarCliente(clienteDTO.converteParaModel());
        return new ResponseEntity<>(ClienteDTO.converteParaDto(clienteSalvo),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClienteDTO> bucarPorCpf(String cpf) {
        Cliente clienteEncontraddo = this.clienteService.buscarPorCpf(cpf);
        return new ResponseEntity<>(ClienteDTO.converteParaDto(clienteEncontraddo),
                HttpStatus.CREATED);
    }
}
