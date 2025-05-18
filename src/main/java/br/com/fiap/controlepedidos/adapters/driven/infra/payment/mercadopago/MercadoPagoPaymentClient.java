package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago;

import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentResponseDTO;
import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class MercadoPagoPaymentClient implements IPaymentGateway {

    private final MercadoPagoProperties properties;
    private final ObjectMapper objectMapper;

    public MercadoPagoPaymentClient(final MercadoPagoProperties properties,
                                    final ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public MercadoPagoPaymentResponseDTO generatePixQrCodeMercadoPago(MercadoPagoPaymentRequestDTO request) {
        try {
            URL url = new URL(properties.getEndpoint());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", properties.getAccessToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = objectMapper.writeValueAsString(request);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int statusCode = conn.getResponseCode();

            InputStream inputStream = (statusCode < 400)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line.trim());
                }

                return objectMapper.readValue(responseBuilder.toString(), MercadoPagoPaymentResponseDTO.class);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API do Mercado Pago: " + e.getMessage(), e);
        }
    }
}
