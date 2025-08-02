package br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

import java.util.UUID;

public record PaymentDataResponseDTO(UUID orderID, String paymentStatus, String provider, String qrCode,
                                     int totalCents) {

    public static PaymentDataResponseDTO fromDomain(Payment payment) {
        return new PaymentDataResponseDTO(payment.getOrder().getId(),
                payment.getPaymentStatus().getDescription(),
                payment.getProvider(),
                payment.getQrCode(),
                payment.getTotalCents()
        );
    }
}