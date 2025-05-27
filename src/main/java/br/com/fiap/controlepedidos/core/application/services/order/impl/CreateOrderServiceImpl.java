package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.order.CreateOrderService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CreateOrderServiceImpl implements CreateOrderService {

    private final FindCartService findCartService;
    private final IOrderRepository orderRepository;

    public CreateOrderServiceImpl(FindCartService findCartService, IOrderRepository orderRepository) {
        this.findCartService = findCartService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(UUID cartId) throws Exception {
        try {
            Order newOrder = new Order();
            Cart cart = findCartService.findById(cartId);

            newOrder.setCart(cart);
            newOrder.setCustomer(cart.getCustomer()); //TODO ideal é implementar o get customer by ID
            newOrder.setTotalCents(cart.getTotalCents());

            orderRepository.save(newOrder);

            return newOrder;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
