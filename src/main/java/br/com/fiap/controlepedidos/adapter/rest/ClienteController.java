package br.com.fiap.controlepedidos.adapter.rest;

import br.com.fiap.controlepedidos.adapter.rest.contract.ClienteApi;
import br.com.fiap.controlepedidos.adapter.rest.dto.ClienteDTO;
import br.com.fiap.controlepedidos.adapter.rest.dto.RespostaPaginada;
import br.com.fiap.controlepedidos.application.service.ClienteService;
import br.com.fiap.controlepedidos.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
    public ResponseEntity<ClienteDTO> bucarPorCpf(final String cpf) {
        Cliente clienteEncontraddo = this.clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(ClienteDTO.converteParaDto(clienteEncontraddo));
    }

    @Override
    public ResponseEntity<RespostaPaginada<ClienteDTO>> buscarTodos(final Pageable pageable) {
        Page<ClienteDTO> result = this.clienteService.buscarTodos(pageable).map(ClienteDTO::converteParaDto);
        return ResponseEntity.ok(RespostaPaginada.of(result));

    }

    @Override
    public ResponseEntity<Void> apagar(@PathVariable final UUID id) {
        this.clienteService.apagar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
