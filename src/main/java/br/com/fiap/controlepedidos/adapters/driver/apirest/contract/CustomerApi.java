package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.CustomerDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(CustomerApi.BASE_URL)
public interface CustomerApi {
    String BASE_URL = "/api/v1/customers";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomerDTO> create(@RequestBody @Valid CustomerDTO customerDTO);

    @GetMapping(value = "{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomerDTO> findByCPF(@PathVariable String cpf);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<CustomerDTO>> findAll(Pageable pageable);

    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteById(UUID id);
}
