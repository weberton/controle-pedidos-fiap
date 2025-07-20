package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago;

import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentItemDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentResponseDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.exceptions.MercadoPagoConnectionException;
import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    public Payment generatePixQrCodeMercadoPago(Order order) {
        try {
            URL url = new URL(properties.getEndpoint());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", properties.getAccessToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = objectMapper.writeValueAsString(parseOrdertoRequestDTO(order));

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

                return parsePaymentDetails(objectMapper.readValue(responseBuilder.toString(), MercadoPagoPaymentResponseDTO.class));
            }

        } catch (Exception e) {
            throw new MercadoPagoConnectionException("Erro ao chamar a API do Mercado Pago: " + e.getMessage(), e);
        }
    }

    private MercadoPagoPaymentRequestDTO parseOrdertoRequestDTO(Order order) {
        MercadoPagoPaymentRequestDTO requestBody = new MercadoPagoPaymentRequestDTO();
        requestBody.setOrderId(String.valueOf(order.getId()));
        requestBody.setTitle("Titulo da Compra");
        requestBody.setDescription("Order: " + order.getId() + " para o carrinho" + order.getCart().getId());
        requestBody.setCallBackUrl("https://www.yourserver.com/notifications");
        requestBody.setTotalAmount(requestBody.convertTotalCentsToDTO(order.getTotalCents()));
        requestBody.setItems(getPaymentItems(order));

        return requestBody;
    }

    private List<MercadoPagoPaymentItemDTO> getPaymentItems(Order order) {
        List<MercadoPagoPaymentItemDTO> requestBodyItem = new ArrayList<>();

        for (CartItem item : order.getCart().getItems()) {

            MercadoPagoPaymentItemDTO dto = new MercadoPagoPaymentItemDTO();

            dto.setProductId(String.valueOf(item.getProduct().getId()));
            dto.setCategory(String.valueOf(item.getProduct().getCategory()));
            dto.setTitle(item.getProduct().getName());
            dto.setDescription(item.getProduct().getDescription());
            dto.setProductPrice(dto.convertTotalCentsToDTO(item.getProduct().getPrice()));
            dto.setQuantity(item.getQuantity());
            dto.setProductUnit("UN");
            dto.setTotalAmount(dto.convertTotalCentsToDTO(item.getSubtotalCents()));

            requestBodyItem.add(dto);
        }
        return requestBodyItem;
    }

    private Payment parsePaymentDetails(MercadoPagoPaymentResponseDTO responseDTO) {

        Payment payment = new Payment();

        payment.setProvider("Mercado Pago PIX QR CODE");
        payment.setQrCode(responseDTO.getQrCodeData());

        return payment;
    }
}