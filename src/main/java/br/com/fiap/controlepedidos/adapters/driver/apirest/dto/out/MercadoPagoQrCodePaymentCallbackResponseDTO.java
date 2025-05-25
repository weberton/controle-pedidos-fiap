package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MercadoPagoQrCodePaymentCallbackResponseDTO(
        UUID paymentId,
        String customerName,
        UUID orderId,
        int orderNumber,
        String status) {


    public static MercadoPagoQrCodePaymentCallbackResponseDTO fromDomain(Payment payment) {
        return new MercadoPagoQrCodePaymentCallbackResponseDTO(payment.getId(), payment.getOrder().getCustomer().getName()
                , payment.getOrder().getId(), payment.getOrder().getOrderNumber(), payment.getPaymentStatus().getDescription());
    }
}