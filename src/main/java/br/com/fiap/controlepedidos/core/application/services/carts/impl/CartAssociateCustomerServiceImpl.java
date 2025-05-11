package br.com.fiap.controlepedidos.core.application.services.carts.impl;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.CartAssociateCustomerService;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.FindCartServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.CartAlreadyAssociatedException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CartAssociateCustomerServiceImpl implements CartAssociateCustomerService {
    private final FindCartService findCartService;
    private final FindCustomerByIdService findCustomerByIdService;
    private final CartsRepository cartsRepository;

    public CartAssociateCustomerServiceImpl(final FindCartServiceImpl findCartService,
                                            final FindCustomerByIdService findCustomerByIdService,
                                            final CartsRepository cartsRepository) {
        this.findCartService = findCartService;
        this.findCustomerByIdService = findCustomerByIdService;
        this.cartsRepository = cartsRepository;
    }

    @Override
    public Cart associateCustomer(final UUID cartId,
                                  final UUID customerId) {
        Cart cart = findCartService.findById(cartId);

        if (hasCustomer(cart, customerId)) {
            throw new CartAlreadyAssociatedException("Cart %s já está associado a um customer.".formatted(cartId));
        }
        Customer customer = findCustomerByIdService.findById(customerId);
        cart.setCustomer(customer);
        return this.cartsRepository.save(cart);
    }

    private boolean hasCustomer(final Cart cart,
                                final UUID customerId) {
        return Objects.nonNull(cart.getCustomer()) && !customerId.equals(cart.getCustomer().getId());
    }
}
