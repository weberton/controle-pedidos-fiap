package br.com.fiap.controlepedidos.core.application.services.carts.impl;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.DeleteCartService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCartServiceImpl implements DeleteCartService {
    private final CartsRepository cartsRepository;

    public DeleteCartServiceImpl(CartsRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }

    @Override
    public void deleteById(UUID cartId) {
        this.cartsRepository.deleteById(cartId);
    }
}
