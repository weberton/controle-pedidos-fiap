package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.CartsApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CartAssociateCustomerRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateCartRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateItemRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.UpdateItemQuantityRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.CartResponseDto;
import br.com.fiap.controlepedidos.core.application.services.carts.CartAssociateCustomerService;
import br.com.fiap.controlepedidos.core.application.services.carts.CreateUpdateCartService;
import br.com.fiap.controlepedidos.core.application.services.carts.DeleteCartService;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CartsController implements CartsApi {
    private final CreateUpdateCartService createUpdateCartService;
    private final FindCartService findCartService;
    private final CartAssociateCustomerService cartAssociateCustomerService;
    private final DeleteCartService deleteCartService;

    public CartsController(final CreateUpdateCartService createUpdateCartService,
                           final FindCartService findCartService,
                           final CartAssociateCustomerService cartAssociateCustomerService,
                           final DeleteCartService deleteCartService) {
        this.createUpdateCartService = createUpdateCartService;
        this.findCartService = findCartService;
        this.cartAssociateCustomerService = cartAssociateCustomerService;
        this.deleteCartService = deleteCartService;
    }

    @Override
    public ResponseEntity<CartResponseDto> createCart(final CreateCartRequest createCartRequest) {
        Cart cart = createUpdateCartService.create(createCartRequest.toDomain(), createCartRequest.customerId());
        return new ResponseEntity<>(CartResponseDto.fromDomain(cart), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CartResponseDto> findById(final UUID cartId) {
        return ResponseEntity.ok((CartResponseDto.fromDomain(findCartService.findById(cartId))));
    }

    @Override
    public ResponseEntity<CartResponseDto> addItem(final UUID cartId,
                                                   final CreateItemRequest item) {
        return ResponseEntity.ok(CartResponseDto.fromDomain(createUpdateCartService.addItem(cartId, item.toDomain())));
    }

    @Override
    public ResponseEntity<CartResponseDto> associateCustomer(final UUID cartId,
                                                             final CartAssociateCustomerRequest cartAssociateCustomerRequest) {
        return ResponseEntity.ok(CartResponseDto.fromDomain(cartAssociateCustomerService.associateCustomer(cartId,
                cartAssociateCustomerRequest.customerId())));
    }

    @Override
    public ResponseEntity<Void> deleteCartById(UUID cartId) {
        deleteCartService.deleteById(cartId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CartResponseDto> removeItem(final UUID cartId,
                                                      final UUID itemId) {
        return ResponseEntity.ok(CartResponseDto.fromDomain(createUpdateCartService.removeItem(cartId, itemId)));
    }

    @Override
    public ResponseEntity<CartResponseDto> updateItemQuantity(final UUID cartId,
                                                              final UUID itemId,
                                                              final UpdateItemQuantityRequest updateItemQuantityRequest) {
        return ResponseEntity.ok(CartResponseDto.fromDomain(createUpdateCartService.updateQuantity(cartId, itemId, updateItemQuantityRequest.quantity())));
    }


}