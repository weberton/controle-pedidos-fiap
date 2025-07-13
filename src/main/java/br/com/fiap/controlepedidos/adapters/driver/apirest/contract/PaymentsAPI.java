package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.PaymentStatusResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping(PaymentsAPI.BASE_URL)
public interface PaymentsAPI {
    String BASE_URL = "/api/v1/payments";

    @GetMapping(value = "{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentStatusResponseDTO> findByOrderId(@PathVariable UUID orderId);
}