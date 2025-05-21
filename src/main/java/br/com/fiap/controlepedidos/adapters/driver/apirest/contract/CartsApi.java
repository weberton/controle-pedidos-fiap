package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CartAssociateCustomerRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateCartRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateItemRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.UpdateItemQuantityRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.CartResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(CartsApi.BASE_URL)
public interface CartsApi {
    String BASE_URL = "/api/v1/carts";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> createCart(@RequestBody @Valid CreateCartRequest item);

    @GetMapping(value = "{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> findById(@PathVariable UUID cartId);

    @PostMapping(value = "{cartId}/items", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> addItem(@PathVariable UUID cartId,
                                            @RequestBody @Valid CreateItemRequest item);

    @PostMapping(value = "{cartId}/identify", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> associateCustomer(@PathVariable UUID cartId,
                                                      @RequestBody @Valid CartAssociateCustomerRequest cartAssociateCustomerRequest);

    @DeleteMapping("{cartId}")
    ResponseEntity<Void> deleteCartById(@PathVariable UUID cartId);

    @DeleteMapping(value = "{cartId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> removeItem(@PathVariable UUID cartId, @PathVariable UUID itemId);

    @PatchMapping(value = "{cartId}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CartResponseDto> updateItemQuantity(@PathVariable UUID cartId,
                                                       @PathVariable UUID itemId,
                                                       @RequestBody @Valid UpdateItemQuantityRequest updateItemQuantityRequest);
}
