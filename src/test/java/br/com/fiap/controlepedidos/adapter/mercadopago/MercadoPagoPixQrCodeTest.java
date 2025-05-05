package br.com.fiap.controlepedidos.adapter.mercadopago;

import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentItemDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentRequestDTO;
import br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto.MercadoPagoPaymentResponseDTO;
import br.com.fiap.controlepedidos.core.application.ports.IPaymentGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MercadoPagoPixQrCodeTest {

    //Arrange
    @Autowired
    private IPaymentGateway paymentGateway;

    @Test
    void Should_Generate_QRCodeData_MPOderId() {
        //Act
        MercadoPagoPaymentResponseDTO response = paymentGateway.generatePixQrCodeMercadoPago(generateOrderMatchingWithTotalValueFromItems());

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getMercadoPagoOrderId()).isNotEmpty();
        assertThat(response.getQrCodeData()).isNotEmpty();
        assertThat(response.getStatusCode()).isNullOrEmpty();
        assertThat(response.getErrorTitle()).isNullOrEmpty();
        assertThat(response.getErrorDescription()).isNullOrEmpty();
    }

    private MercadoPagoPaymentRequestDTO generateOrderMatchingWithTotalValueFromItems() {
        List<MercadoPagoPaymentItemDTO> items = new ArrayList<>();

        MercadoPagoPaymentItemDTO firstItem = new MercadoPagoPaymentItemDTO(
                "productId01",
                "productCategory01",
                "productName01",
                "productDesc01",
                50.00F,
                1,
                "productUnit01",
                50.00F
        );
        items.add(firstItem);

        MercadoPagoPaymentItemDTO secondItem = new MercadoPagoPaymentItemDTO(
                "productId02",
                "productCategory01",
                "productName02",
                "productDesc02",
                50.00F,
                1,
                "productUnit01",
                50.00F
        );
        items.add(secondItem);

        return new MercadoPagoPaymentRequestDTO(
                "OrderId",
                "OrderTitle",
                "OrderDescription",
                "https://www.yourserver.com/notifications",
                100.00F,
                items
        );
    }

    @Test
    void Should_Generate_400_ResponseError() {
        //Act
        MercadoPagoPaymentResponseDTO response = paymentGateway.generatePixQrCodeMercadoPago(generateOrderUnmatchingWithTotalValueFromItems());

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getMercadoPagoOrderId()).isNullOrEmpty();
        assertThat(response.getQrCodeData()).isNullOrEmpty();
        assertThat(response.getStatusCode()).isNotEmpty();
        assertThat(response.getStatusCode()).isEqualTo("400");
        assertThat(response.getErrorTitle()).isNotEmpty();
        assertThat(response.getErrorDescription()).isNotEmpty();
    }

    private MercadoPagoPaymentRequestDTO generateOrderUnmatchingWithTotalValueFromItems() {
        List<MercadoPagoPaymentItemDTO> items = new ArrayList<>();

        MercadoPagoPaymentItemDTO firstItem = new MercadoPagoPaymentItemDTO(
                "productId01",
                "productCategory01",
                "productName01",
                "productDesc01",
                50.00F,
                1,
                "productUnit01",
                50.00F
        );
        items.add(firstItem);

        MercadoPagoPaymentItemDTO secondItem = new MercadoPagoPaymentItemDTO(
                "productId02",
                "productCategory01",
                "productName02",
                "productDesc02",
                50.00F,
                1,
                "productUnit01",
                50.00F
        );
        items.add(secondItem);

        return new MercadoPagoPaymentRequestDTO(
                "OrderId",
                "OrderTitle",
                "OrderDescription",
                "https://www.yourserver.com/notifications",
                110.00F,
                items
        );
    }
}