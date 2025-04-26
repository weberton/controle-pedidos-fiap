package br.com.fiap.controlepedidos.adapter.rest.contract;

import br.com.fiap.controlepedidos.adapter.rest.dto.ClienteDTO;
import br.com.fiap.controlepedidos.adapter.rest.dto.RespostaPaginada;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(ClienteApi.BASE_URL)
public interface ClienteApi {
    String BASE_URL = "/api/v1/clientes";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteDTO> criar(@RequestBody @Valid ClienteDTO clienteDTO);

    @GetMapping(value = "{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteDTO> bucarPorCpf(@PathVariable String cpf);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RespostaPaginada<ClienteDTO>> buscarTodos(Pageable pageable);

    @DeleteMapping("{id}")
    ResponseEntity<Void> apagar(UUID id);
}
