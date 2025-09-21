package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim;

import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.in.AddAccountRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.in.AuthAccountRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.out.AddAccountResponseDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.out.GetAccountResponseDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception.APIMCommunicationException;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception.EntityRequiredException;
import br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception.MissingRequiredValueException;
import br.com.fiap.controlepedidos.core.application.ports.IAzureAPIMGateway;
import br.com.fiap.controlepedidos.core.domain.entities.Account;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

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
        } catch (JsonProcessingException e) {
            throw new APIMCommunicationException("Erro ao serializar/deserializar JSON do APIM", e);
        } catch (IOException e) {
            throw new APIMCommunicationException("Erro de comunicação com o APIM", e);
        } catch (Exception e) {
            throw new APIMCommunicationException("Erro inesperado ao chamar o APIM", e);
        }
    }

    @Override
    public Account createCustomerAccountByCPF(Customer customer) {
        validateCustomer(customer, false);

        AddAccountRequestDTO request = AddAccountRequestDTO.builder()
                .cpf(customer.getCpf())
                .name(customer.getName())
                .build();

        AddAccountResponseDTO response = sendRequest(
                properties.getAddcpfendpoint(),
                properties.getAddcpfatuhvalue(),
                request,
                AddAccountResponseDTO.class
        );

        return parseAccountToAddCpfResponseDTO(response)
                .orElseThrow(() -> new APIMCommunicationException("Falha ao criar conta no APIM"));
    }

    @Override
    public boolean authenticateCustomerAccountByCPF(Customer customer) {
        validateCustomer(customer, true);

        AuthAccountRequestDTO request = new AuthAccountRequestDTO();
        request.setUserId(customer.getAccountid());

        GetAccountResponseDTO response = sendRequest(
                properties.getAuthcpfendpoint(),
                properties.getAuthcpfauthvalue(),
                request,
                GetAccountResponseDTO.class
        );

        return parseAccountFoundResponseDTO(response);
    }

    private Optional<Account> parseAccountToAddCpfResponseDTO(AddAccountResponseDTO dto) {
        if (dto == null || isNullOrEmpty(dto.getUserId())) {
            throw new APIMCommunicationException("APIM - Resposta inválida ao criar conta.");
        }
        return "201".equals(dto.getStatusCode())
                ? Optional.of(Account.builder().userId(dto.getUserId()).build())
                : Optional.empty();
    }

    private boolean parseAccountFoundResponseDTO(GetAccountResponseDTO dto) {
        if (dto == null || isNullOrEmpty(dto.getUserId())) {
            throw new APIMCommunicationException("APIM - Resposta inválida ao autenticar conta.");
        }
        return Objects.equals(dto.getStatusCode(), "200");
    }

    private void validateCustomer(Customer customer, boolean requireAccountId) {
        if (customer == null) throw new EntityRequiredException("O cliente não pode ser nulo");
        if (isNullOrEmpty(customer.getCpf())) throw new MissingRequiredValueException("CPF não pode ser nulo ou vazio");
        if (!requireAccountId && isNullOrEmpty(customer.getName())) {
            throw new MissingRequiredValueException("Nome não pode ser nulo ou vazio");
        }
        if (requireAccountId && isNullOrEmpty(customer.getAccountid())) {
            throw new MissingRequiredValueException("AccountId não pode ser nulo ou vazio");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}