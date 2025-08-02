package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.PaymentsAPI;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.PaymentStatusResponseDTO;
import br.com.fiap.controlepedidos.core.application.services.payment.IFindPaymentByOrderId;
import br.com.fiap.controlepedidos.core.domain.entities.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PaymentsController implements PaymentsAPI {

    private final IFindPaymentByOrderId findPaymentByOrderIdService;

    public PaymentsController(IFindPaymentByOrderId findPaymentByOrderIdService) {
        this.findPaymentByOrderIdService = findPaymentByOrderIdService;
    }


    @Override
    public ResponseEntity<PaymentStatusResponseDTO> findByOrderId(UUID orderId) {
        Payment payment = findPaymentByOrderIdService.findPayment(orderId);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        PaymentStatusResponseDTO response = PaymentStatusResponseDTO.fromDomain(payment);

        return ResponseEntity.ok(response);
    }
}