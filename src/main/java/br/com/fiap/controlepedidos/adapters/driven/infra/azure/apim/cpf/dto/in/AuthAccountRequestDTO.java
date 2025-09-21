package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.cpf.dto.in;


import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthAccountRequestDTO {
    private String userId;
}
