package br.com.fiap.controlepedidos.core.application.services.order.impl;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.order.GetAllOrdersInProcessService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
public class GetAllOrdersInProcessServiceImpl implements GetAllOrdersInProcessService {

    private final IOrderRepository orderRepository;
    private final FindCustomerByIdService findCustomerByIdService;

    public GetAllOrdersInProcessServiceImpl(IOrderRepository orderRepository, FindCustomerByIdService findCustomerByIdService) {
        this.orderRepository = orderRepository;
        this.findCustomerByIdService = findCustomerByIdService;
    }

    @Override
    public Page<Order> getAllOrdersInProcess(Pageable pageable) {

        Map<OrderStatus, Integer> statusPriority = Map.of(
                OrderStatus.READY, 0,
                OrderStatus.INPREP, 1,
                OrderStatus.RECEIVED, 2
        );

        List<Order> allOrders = orderRepository.findAll();

        List<Order> filteredAndSortedOrders = allOrders.stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.DONE)
                .filter(order -> order.getOrderStatus() != OrderStatus.CREATED)
                .sorted(Comparator
                        .comparingInt((Order order) -> statusPriority.getOrDefault(order.getOrderStatus(), Integer.MAX_VALUE))
                        .thenComparing(Order::getCreatedAt))
                .toList();

        filteredAndSortedOrders.forEach(order -> {
            if (order.getCustomer() != null) {
                Customer customer = findCustomerByIdService.findById(order.getCustomer().getId());
                order.setCustomer(customer);
            }
        });

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredAndSortedOrders.size());
        List<Order> paginatedOrders = filteredAndSortedOrders.subList(start, end);

        return new PageImpl<>(paginatedOrders, pageable, filteredAndSortedOrders.size());
    }
}