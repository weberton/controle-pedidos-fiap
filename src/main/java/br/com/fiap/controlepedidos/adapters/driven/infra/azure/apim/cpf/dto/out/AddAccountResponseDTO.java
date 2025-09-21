package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.out;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddAccountResponseDTO {
    private String userId;
    private String statusCode;
    private String message;
    private String details;
}