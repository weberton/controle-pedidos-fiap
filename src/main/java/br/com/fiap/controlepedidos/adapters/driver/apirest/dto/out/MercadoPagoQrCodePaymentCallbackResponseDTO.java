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
        String customerName = "N/A";
        if (payment.getOrder() != null && payment.getOrder().getCustomer() != null && payment.getOrder().getCustomer().getName() != null) {
            customerName = payment.getOrder().getCustomer().getName();
        }

        return new MercadoPagoQrCodePaymentCallbackResponseDTO(
                payment.getId(),
                customerName,
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getOrder() != null ? payment.getOrder().getOrderNumber() : null,
                payment.getPaymentStatus() != null ? payment.getPaymentStatus().getDescription() : null
        );
    }
}