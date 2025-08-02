package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.order.impl.GetAllOrdersDoneServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllOrdersDoneServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private GetAllOrdersDoneServiceImpl service;

    @Test
    void getAll_ShouldReturnPageOfDoneOrders() {

        Pageable pageable = PageRequest.of(0, 10);

        Order order1 = new Order();
        order1.setId(java.util.UUID.randomUUID());
        order1.setOrderStatus(OrderStatus.DONE);

        Order order2 = new Order();
        order2.setId(java.util.UUID.randomUUID());
        order2.setOrderStatus(OrderStatus.DONE);

        Page<Order> page = new PageImpl<>(List.of(order1, order2), pageable, 2);

        when(orderRepository.findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.DONE, pageable)).thenReturn(page);

        Page<Order> result = service.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(order -> order.getOrderStatus() == OrderStatus.DONE);

        verify(orderRepository, times(1)).findByOrderStatusOrderByUpdatedAtAsc(OrderStatus.DONE, pageable);
    }
}
