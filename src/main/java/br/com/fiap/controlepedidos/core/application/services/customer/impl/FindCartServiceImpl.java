package br.com.fiap.controlepedidos.core.application.services.customer.impl;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindCartServiceImpl implements FindCartService {
    private final CartsRepository cartsRepository;

    public FindCartServiceImpl(CartsRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }

    @Override
    public Cart findById(UUID id) {
        return cartsRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Cart %s not found."
                .formatted(id)));
    }
}
