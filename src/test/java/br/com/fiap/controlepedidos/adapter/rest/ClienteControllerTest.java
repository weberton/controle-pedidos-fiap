package br.com.fiap.controlepedidos.adapter.rest;

import br.com.fiap.controlepedidos.adapter.rest.contract.ClienteApi;
import br.com.fiap.controlepedidos.adapter.rest.dto.ClienteDTO;
import br.com.fiap.controlepedidos.application.service.ClienteService;
import br.com.fiap.controlepedidos.common.RegistroExistenteException;
import br.com.fiap.controlepedidos.common.RegistroNaoEncontradoException;
import br.com.fiap.controlepedidos.common.RestExceptionHandler;
import br.com.fiap.controlepedidos.domain.model.Cliente;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    public static final String CPF = "89405972812";
    public static final String NOME_COMPLETO = "Nome completo";
    public static final String MAIL = "email@gmail.com";

    @Mock
    private ClienteService clienteService;
    @InjectMocks
    private ClienteController clienteController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ClienteDTO clienteDto;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
        this.clienteDto = new ClienteDTO(UUID.randomUUID(), CPF, NOME_COMPLETO, MAIL);
    }

    @Test
    void criar_quandoCpfEmailNaoExistem_retornaStatusCreated() throws Exception {

        when(clienteService.criarCliente(any())).thenReturn(clienteDto.converteParaModel());

        mockMvc.perform(post(ClienteApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void criar_quandoServiceLancerRegistroExistente_retornaStatus400() throws Exception {
        when(clienteService.criarCliente(any())).thenThrow(RegistroExistenteException.class);
        mockMvc.perform(post(ClienteApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void criar_quandoCpfInvalido_retornaStatus400() throws Exception {
        ClienteDTO clienteComCpfInvalido = new ClienteDTO(this.clienteDto.id(),
                "123",
                this.clienteDto.nome(),
                this.clienteDto.email());

        mockMvc.perform(post(ClienteApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComCpfInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(ClienteDTO.CPF_INVALIDO)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"invalidEmail@", ""})
    void criar_quandoEmailInvalido_retornaStatus400(String invalidEmail) throws Exception {
        ClienteDTO clienteComEmailInvalido = new ClienteDTO(this.clienteDto.id(),
                this.clienteDto.cpf(),
                this.clienteDto.nome(),
                invalidEmail);

        mockMvc.perform(post(ClienteApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComEmailInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(ClienteDTO.EMAL_INVALIDO)));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void criar_quandoNomeInvalido_retornaStatus400(String invalidName) throws Exception {
        ClienteDTO clienteComEmailInvalido = new ClienteDTO(this.clienteDto.id(),
                this.clienteDto.cpf(),
                invalidName,
                this.clienteDto.email());

        mockMvc.perform(post(ClienteApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(clienteComEmailInvalido)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(ClienteDTO.NOME_INVALIDO)));
    }

    @Test
    void buscarPorCpf_quandoCpfEncontrado_retornaCliente() throws Exception {
        Cliente cliente = clienteDto.converteParaModel();

        when(clienteService.buscarPorCpf(any())).thenReturn(cliente);

        mockMvc.perform(get(ClienteApi.BASE_URL + "/" + cliente.getCpf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void buscarPorCpf_quandoCpfNaoEncontrado_retorna404() throws Exception {
        Cliente cliente = clienteDto.converteParaModel();

        when(clienteService.buscarPorCpf(any())).thenThrow(RegistroNaoEncontradoException.class);

        mockMvc.perform(get(ClienteApi.BASE_URL + "/" + cliente.getCpf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void apagar_retornaOk() throws Exception {
        Cliente cliente = clienteDto.converteParaModel();

        doNothing().when(clienteService).apagar(any());

        mockMvc.perform(delete(ClienteApi.BASE_URL + "/" + cliente.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    private String toJson(ClienteDTO clienteDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(clienteDto);
    }

}
