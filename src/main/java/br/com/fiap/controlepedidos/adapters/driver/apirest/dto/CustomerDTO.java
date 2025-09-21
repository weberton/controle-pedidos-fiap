package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerDTO(UUID id,
                          @CPF(message = INVALID_CPF) String cpf,
                          @NotEmpty(message = INVALID_NAME) String nome,
                          @NotEmpty(message = INVALID_EMAIL) @Email(message = INVALID_EMAIL) String email,
                          String accountid
) {

    public static final String INVALID_EMAIL = "Emal inválido";
    public static final String INVALID_CPF = "CPF inválido.";
    public static final String INVALID_NAME = "Nome é obrigatório.";

    public Customer convertToModel() {
        return Customer.builder()
                .id(this.id())
                .cpf(this.cpf())
                .name(this.nome)
                .email(this.email())
                .build();

    }

    public static CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(customer.getId(),
                customer.getCpf(),
                customer.getName(),
                customer.getEmail(),
                customer.getAccountid());
    }

}
