package br.com.fiap.controlepedidos.core.application.services.order;

import br.com.fiap.controlepedidos.core.application.ports.IOrderRepository;
import br.com.fiap.controlepedidos.core.application.services.order.impl.FindOrderByIdServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private FindOrderByIdServiceImpl findOrderByIdService;

    private UUID existingOrderId;
    private Order dummyOrder;

    @BeforeEach
    void setup() {
        existingOrderId = UUID.randomUUID();

        dummyOrder = new Order();
        dummyOrder.setId(existingOrderId);
        dummyOrder.setTotalCents(10_000);
    }

    @Test
    void getById_whenOrderExists_shouldReturnOrder() {
        when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(dummyOrder));

        Order returned = findOrderByIdService.getById(existingOrderId);

        assertThat(returned).isNotNull();
        assertSame(dummyOrder, returned);

        verify(orderRepository).findById(existingOrderId);
    }

    @Test
    void getById_whenOrderDoesNotExist_shouldThrowRecordNotFoundException() {
        UUID missingOrderId = UUID.randomUUID();
        when(orderRepository.findById(missingOrderId)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> findOrderByIdService.getById(missingOrderId));

        String expectedMessage = "Pedido com ID %s não encontrado.".formatted(missingOrderId);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);

        verify(orderRepository).findById(missingOrderId);
        verifyNoMoreInteractions(orderRepository);
    }
}
