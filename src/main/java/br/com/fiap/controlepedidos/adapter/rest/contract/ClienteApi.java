package br.com.fiap.controlepedidos.adapter.rest.contract;

import br.com.fiap.controlepedidos.adapter.rest.dto.ClienteDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ClienteApi.BASE_URL)
public interface ClienteApi {
    String BASE_URL = "/api/v1/clientes";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteDTO> criar(@RequestBody @Valid ClienteDTO clienteDTO);

    @GetMapping(value = "{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClienteDTO> bucarPorCpf(@PathVariable String cpf);
}
