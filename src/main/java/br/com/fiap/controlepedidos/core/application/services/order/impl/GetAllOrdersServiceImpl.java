package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.GetAllOrdersService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetAllOrdersServiceImpl implements GetAllOrdersService {

    private final IOrderRepository orderRepository;
    private final FindCustomerByIdService customerService;

    public GetAllOrdersServiceImpl(IOrderRepository orderRepository, FindCustomerByIdService customerService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Override
    public Page<Order> getAll(Pageable pageable) throws Exception {

        Page<Order> orders = orderRepository.findAll(pageable);

        orders.forEach(order -> {
            if (order.getCustomer() != null) {
                Customer customer = customerService.findById(order.getCustomer().getId());
                order.setCustomer(customer);
            }
        });

        return orders;
    }
}
