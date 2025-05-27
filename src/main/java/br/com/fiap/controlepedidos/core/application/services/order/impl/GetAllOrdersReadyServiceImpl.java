package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.GetAllOrdersReadyService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class GetAllOrdersReadyServiceImpl implements GetAllOrdersReadyService {

    private final IOrderRepository orderRepository;
    private final FindCustomerByIdService findCustomerByIdService;

    public GetAllOrdersReadyServiceImpl(IOrderRepository orderRepository, FindCustomerByIdService findCustomerByIdService) {
        this.orderRepository = orderRepository;
        this.findCustomerByIdService = findCustomerByIdService;
    }

    @Override
    public Page<Order> getAll(Pageable pageable) throws Exception {

        Page<Order> orders = orderRepository.findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.READY, pageable);

        orders.forEach(order -> {
            if (order.getCustomer().getId() != null) {
                Customer customer = findCustomerByIdService.findById(order.getCustomer().getId());
                order.setCustomer(customer);
            }
        });

        return orders;
    }
}
