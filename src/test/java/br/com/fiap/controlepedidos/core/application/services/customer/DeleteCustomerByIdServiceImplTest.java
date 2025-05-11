package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.DeleteCustomerByIdServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerByIdServiceImplTest {
    @Mock
    private ICustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerByIdServiceImpl deleteCustomerById;

    @Test
    void delete_shouldCallRepository() {
        UUID customerId = UUID.randomUUID();
        Mockito.doNothing().when(customerRepository).deleteById(any());

        this.deleteCustomerById.delete(customerId);
        verify(customerRepository).deleteById(customerId);
    }
}
