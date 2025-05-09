package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(ProductApi.BASE_URL)
public interface ProductApi {

    String BASE_URL = "/api/v1/products";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDTO productDTO);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDTO> findById(@PathVariable UUID id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<ProductDTO>> findAll(@PathVariable Pageable pageable);

    @PutMapping()
    ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO);

    @DeleteMapping("{id}")
    ResponseEntity<Void> deleteById(UUID id);

}
