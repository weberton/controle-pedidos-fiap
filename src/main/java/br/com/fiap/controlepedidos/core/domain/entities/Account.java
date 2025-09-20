package br.com.fiap.controlepedidos.core.domain.entities;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    private String userId;
    private String cpf;
    private String name;
    private String userAccount;
}