package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.CheckoutApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.DoCheckoutRequestDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.MercadoPagoQRCodePaymentCallbackDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.MercadoPagoQrCodePaymentCallbackResponseDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.PaymentDataResponseDTO;
import br.com.fiap.controlepedidos.core.application.services.checkout.ConfirmQRCodePaymentOrder;
import br.com.fiap.controlepedidos.core.application.services.checkout.StartCheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController implements CheckoutApi {
    private final StartCheckoutService startCheckoutService;
    private final ConfirmQRCodePaymentOrder confirmQRCodePaymentOrder;

    public CheckoutController(StartCheckoutService startCheckoutService, ConfirmQRCodePaymentOrder confirmQRCodePaymentOrder) {
        this.startCheckoutService = startCheckoutService;
        this.confirmQRCodePaymentOrder = confirmQRCodePaymentOrder;
    }

    @Override
    public ResponseEntity<PaymentDataResponseDTO> doCheckout(DoCheckoutRequestDTO cartData) {
        return ResponseEntity.ok(PaymentDataResponseDTO.fromDomain(startCheckoutService.startCheckout(cartData.cartId())));
    }


    @Override
    public ResponseEntity<MercadoPagoQrCodePaymentCallbackResponseDTO> confirmPayment(MercadoPagoQRCodePaymentCallbackDTO callbackDetails) {
        return ResponseEntity.ok(MercadoPagoQrCodePaymentCallbackResponseDTO.fromDomain(confirmQRCodePaymentOrder.confirmQrCodePayment(callbackDetails.orderId(), callbackDetails.paidValue().intValue() )));
    }
}
