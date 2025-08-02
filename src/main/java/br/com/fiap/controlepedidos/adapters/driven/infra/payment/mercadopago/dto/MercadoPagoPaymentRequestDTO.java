package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPaymentRequestDTO {

    @JsonProperty("external_reference")
    private String orderId;

    private String title;
    private String description;

    @JsonProperty("notification_url")
    private String callBackUrl;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private List<MercadoPagoPaymentItemDTO> items;

    public BigDecimal convertTotalCentsToDTO(int totalCents) {
        return new BigDecimal(totalCents).divide(new BigDecimal(100));
    }


}
