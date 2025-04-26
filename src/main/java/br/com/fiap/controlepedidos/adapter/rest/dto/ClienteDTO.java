package br.com.fiap.controlepedidos.adapter.rest.dto;

import br.com.fiap.controlepedidos.domain.model.Cliente;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClienteDTO(UUID id,
                         @CPF String cpf,
                         @NotNull String nome,
                         @NotNull @Email String email) {

    public Cliente converteParaModel() {
        return Cliente.builder()
                .id(this.id())
                .cpf(this.cpf())
                .nome(this.nome())
                .email(this.email())
                .build();

    }

    public static ClienteDTO converteParaDto(Cliente cliente) {
        return new ClienteDTO(cliente.getId(),
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getEmail());
    }

}
