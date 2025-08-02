package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import java.util.UUID;

import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MercadoPagoQrCodePaymentCallbackResponseDTO(
        UUID paymentId,
        String customerName,
        UUID orderId,
        int orderNumber,
        String status,

        String details) {


    public static MercadoPagoQrCodePaymentCallbackResponseDTO fromDomain(Payment payment) {
        String customerName = "N/A";
        String details = "Pagamento não efetuado, verifique o status do pagamento.";
        if (payment.getOrder() != null && payment.getOrder().getCustomer() != null && payment.getOrder().getCustomer().getName() != null) {
            customerName = payment.getOrder().getCustomer().getName();
        }

        if (payment.getPaymentStatus().equals(PaymentStatus.PAID)) {
            details = "Pagamento efetuado.";
        }

        return new MercadoPagoQrCodePaymentCallbackResponseDTO(
                payment.getId(),
                customerName,
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getOrder() != null ? payment.getOrder().getOrderNumber() : null,
                payment.getPaymentStatus() != null ? payment.getPaymentStatus().getDescription() : null,
                details
        );
    }
}