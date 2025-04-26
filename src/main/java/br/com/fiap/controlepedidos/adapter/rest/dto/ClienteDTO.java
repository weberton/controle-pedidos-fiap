package br.com.fiap.controlepedidos.adapter.rest.dto;

import br.com.fiap.controlepedidos.domain.model.Cliente;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClienteDTO(UUID id,
                         @CPF(message = CPF_INVALIDO) String cpf,
                         @NotEmpty(message = NOME_INVALIDO) String nome,
                         @NotEmpty(message = EMAL_INVALIDO) @Email(message = EMAL_INVALIDO) String email) {

    public static final String EMAL_INVALIDO = "Emal inválido";
    public static final String CPF_INVALIDO = "CPF inválido.";
    public static final String NOME_INVALIDO = "Nome é obrigatório.";

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
