package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.CustomerApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.CustomerDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.exceptions.RestExceptionHandler;
import br.com.fiap.controlepedidos.core.application.services.customer.CreateCustomerService;
import br.com.fiap.controlepedidos.core.application.services.customer.DeleteCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindAllCustomersService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByCPFService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    public static final String CPF = "89405972812";
    public static final String NOME_COMPLETO = "Nome completo";
    public static final String MAIL = "email@gmail.com";

    @Mock
    private CreateCustomerService createCustomerService;
    @Mock
    private FindCustomerByCPFService findCustomerByCPF;
    @Mock
    private DeleteCustomerByIdService deleteCustomerById;
    @Mock
    private FindAllCustomersService findAllCustomerService;

    @InjectMocks
    private CustomerController clienteController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CustomerDTO customerDto;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.objectMapper = new ObjectMapper();
        this.customerDto = new CustomerDTO(UUID.randomUUID(), CPF, NOME_COMPLETO, MAIL, "");
    }

    @Test
    void criar_quandoCpfEmailNaoExistem_retornaStatusCreated() throws Exception {

        when(createCustomerService.createCustomer(any())).thenReturn(customerDto.convertToModel());

        mockMvc.perform(post(CustomerApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(customerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void criar_quandoServiceLancerRegistroExistente_retornaStatus400() throws Exception {
        when(createCustomerService.createCustomer(any())).thenThrow(ExistentRecordException.class);
        mockMvc.perform(post(CustomerApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(customerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void criar_quandoCpfInvalido_retornaStatus400() throws Exception {
        CustomerDTO clienteComCpfInvalido = new CustomerDTO(this.customerDto.id(),
                "123",
                this.customerDto.nome(),
                this.customerDto.email(),
                "");

        mockMvc.perform(post(CustomerApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComCpfInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(CustomerDTO.INVALID_CPF)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"invalidEmail@", ""})
    void criar_quandoEmailInvalido_retornaStatus400(String invalidEmail) throws Exception {
        CustomerDTO clienteComEmailInvalido = new CustomerDTO(this.customerDto.id(),
                this.customerDto.cpf(),
                this.customerDto.nome(),
                invalidEmail,
                "");

        mockMvc.perform(post(CustomerApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComEmailInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(CustomerDTO.INVALID_EMAIL)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void criar_quandoNomeInvalido_retornaStatus400(String invalidName) throws Exception {
        CustomerDTO clienteComEmailInvalido = new CustomerDTO(this.customerDto.id(),
                this.customerDto.cpf(),
                invalidName,
                this.customerDto.email(),
                "");

        mockMvc.perform(post(CustomerApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComEmailInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(CustomerDTO.INVALID_NAME)));
    }

    @Test
    void buscarPorCpf_quandoCpfEncontrado_retornaCliente() throws Exception {
        Customer customer = customerDto.convertToModel();

        when(findCustomerByCPF.findByCPF(any())).thenReturn(customer);

        mockMvc.perform(get(CustomerApi.BASE_URL + "/" + customer.getCpf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void buscarPorCpf_quandoCpfNaoEncontrado_retorna404() throws Exception {
        Customer cliente = customerDto.convertToModel();

        when(findCustomerByCPF.findByCPF(any())).thenThrow(RecordNotFoundException.class);

        mockMvc.perform(get(CustomerApi.BASE_URL + "/" + cliente.getCpf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findAll_returnsAllCustomers() throws Exception {
        var customer = customerDto.convertToModel();
        var customers = List.of(customer);

        Page<Customer> responsePage = new PageImpl<>(customers);

        when(findAllCustomerService.findAll(any())).thenReturn(responsePage);

        mockMvc.perform(get("/api/v1/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void apagar_retornaOk() throws Exception {
        Customer cliente = customerDto.convertToModel();

        doNothing().when(deleteCustomerById).delete(cliente.getId());

        mockMvc.perform(delete(CustomerApi.BASE_URL + "/" + cliente.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String toJson(CustomerDTO clienteDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(clienteDto);
    }
}
