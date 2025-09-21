package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim;

import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.in.AddAccountRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.in.AuthAccountRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.out.AddAccountResponseDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.out.GetAccountResponseDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception.APIMCommunicationException;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception.MissingRequiredValueException;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.exceptions.MercadoPagoConnectionException;
import br.com.fiap.controlepedidos.core.application.ports.IAzureAPIMGateway;
import br.com.fiap.controlepedidos.core.domain.entities.Account;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class APIMClient implements IAzureAPIMGateway {

    private final APIMProperties properties;
    private final ObjectMapper objectMapper;

    public APIMClient(APIMProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }
    private <T, R> R sendRequest(
            String endpoint,
            String headerValue,
            T requestDto,
            Class<R> responseType
    ) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty(properties.getAuthkey(), headerValue);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = objectMapper.writeValueAsString(requestDto);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int statusCode = conn.getResponseCode();
            InputStream inputStream = (statusCode < 400) ? conn.getInputStream() : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line.trim());
                }
                return objectMapper.readValue(responseBuilder.toString(), responseType);
            }

        } catch (Exception e) {
            throw new MercadoPagoConnectionException("Erro ao chamar o APIM " + e.getMessage(), e);
        }
    }

    @Override
    public Account createCustomerAccountByCPF(Customer customer) {
        AddAccountRequestDTO request = parseAccountToAddCpfRequestDTO(customer);
        AddAccountResponseDTO response = sendRequest(
                properties.getAddcpfendpoint(),
                properties.getAddcpfatuhvalue(),
                request,
                AddAccountResponseDTO.class
        );
        return parseAccountToAddCpfResponseDTO(response);
    }

    @Override
    public boolean authenticateCustomerAccountByCPF(Customer customer) {
        AuthAccountRequestDTO request = parseCustomerToAuthByCpf(customer);
        GetAccountResponseDTO response = sendRequest(
                properties.getAuthcpfendpoint(),
                properties.getAuthcpfauthvalue(),
                request,
                GetAccountResponseDTO.class
        );
        return parseAccountFoundResponseDTO(response);
    }

    private Account parseAccountToAddCpfResponseDTO(AddAccountResponseDTO addAccountResponseDTO) {
        if (addAccountResponseDTO == null) {
            throw new APIMCommunicationException("APIM - Resposta nula ao criar conta.");
        }
        if (addAccountResponseDTO.getUserId() == null || addAccountResponseDTO.getUserId().isEmpty()) {
            throw new APIMCommunicationException("APIM - UserId nulo ou vazio ao criar conta.");
        }

        if ("201".equals(addAccountResponseDTO.getStatusCode())) {
            return Account.builder()
                    .userId(addAccountResponseDTO.getUserId())
                    .build();
        }
        return null;
    }

    private AddAccountRequestDTO parseAccountToAddCpfRequestDTO(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("O cliente não pode ser nulo");
        }
        if (customer.getCpf() == null || customer.getCpf().isEmpty()) {
            throw new MissingRequiredValueException("O CPF do cliente não pode ser nulo ou vazio");
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new MissingRequiredValueException("O nome do cliente não pode ser nulo ou vazio");
        }

        return AddAccountRequestDTO.builder()
                .cpf(customer.getCpf())
                .name(customer.getName())
                .build();
    }

    private boolean parseAccountFoundResponseDTO(GetAccountResponseDTO getAccountResponseDTO) {
        if (getAccountResponseDTO == null) {
            throw new APIMCommunicationException("APIM - Resposta nula ao autenticar conta.");
        }
        if (getAccountResponseDTO.getUserId() == null || getAccountResponseDTO.getUserId().isEmpty()) {
            throw new APIMCommunicationException("APIM - UserId nulo ou vazio ao autenticar conta.");
        }

        return Objects.equals(getAccountResponseDTO.getStatusCode(), "200");
    }

    private AuthAccountRequestDTO parseCustomerToAuthByCpf(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("O cliente não pode ser nulo");
        }
        if (customer.getCpf() == null || customer.getCpf().isEmpty()) {
            throw new MissingRequiredValueException("O CPF do cliente não pode ser nulo ou vazio");
        }
        if (customer.getAccountid() == null || customer.getAccountid().isEmpty()) {
            throw new MissingRequiredValueException("O AccountId do cliente não pode ser nulo ou vazio");
        }

        AuthAccountRequestDTO dto = new AuthAccountRequestDTO();
        dto.setUserId(customer.getAccountid());
        return dto;
    }
}