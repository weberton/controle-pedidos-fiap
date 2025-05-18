package br.com.fiap.controlepedidos.core.application.services.customer;

import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.impl.FindAllCustomersServiceImpl;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllCustomersServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private FindAllCustomersServiceImpl findAllCustomers;

    @Test
    void findAllCustomers_shouldCallRepository() {
        var page = Pageable.unpaged();
        var customer1 = new Customer(UUID.randomUUID(), "anyCpf", "anyName", "email", 1L);
        var customers = List.of(customer1);
        var expectedResponse = new PageImpl<>(customers);

        when(customerRepository.findAll(page)).thenReturn(expectedResponse);

        var response = findAllCustomers.findAll(page);
        assertThat(response.getTotalElements()).isEqualTo(customers.size());
    }
}
