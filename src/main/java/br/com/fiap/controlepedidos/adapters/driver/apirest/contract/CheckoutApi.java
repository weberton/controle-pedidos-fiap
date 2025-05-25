package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CartAssociateCustomerRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateItemRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.DoCheckoutRequestDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.MercadoPagoQRCodePaymentCallbackDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.MercadoPagoQrCodePaymentCallbackResponseDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.PaymentDataResponseDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.CartResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(CheckoutApi.BASE_URL)
public interface CheckoutApi {
    String BASE_URL = "/api/v1/checkout";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentDataResponseDTO> doCheckout(@RequestBody @Valid DoCheckoutRequestDTO cartData) throws Exception;

    @PostMapping(value = "/confirmMercadoPagoQRCodePayment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MercadoPagoQrCodePaymentCallbackResponseDTO> confirmPayment(@RequestBody @Valid MercadoPagoQRCodePaymentCallbackDTO callbackDetails) throws Exception;
}