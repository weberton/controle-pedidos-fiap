package br.com.fiap.controlepedidos.core.domain.repository;

import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ClienteRepositoryTest {

    //    @Autowired
    private ICustomerRepository _customerRepository;

    public ClienteRepositoryTest(ICustomerRepository customerRepository) {
        this._customerRepository = customerRepository;
    }

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void setup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void saveTest() {
        var cliente = Customer.builder()
                .cpf("12345678912")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();

        //Salvar novo cliente
        var clienteSalvo = this._customerRepository.save(cliente);
        var clienteSalvo1 = this._customerRepository.save(cliente);

        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getName()).isEqualTo(cliente.getName());
        assertThat(clienteSalvo.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteSalvo.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void findByIdTest() {
        var cliente = Customer.builder()
                .cpf("12345678912")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        var clienteSalvo = this._customerRepository.save(cliente);

        // Salvar cliente salvo por ID.
        Customer clienteEncontrado = this._customerRepository.findById(clienteSalvo.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        assertThat(clienteEncontrado.getName()).isEqualTo(cliente.getName());
        assertThat(clienteEncontrado.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteEncontrado.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void findByEmailTest() {
        var cliente = Customer.builder()
                .cpf("12345678912")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        this._customerRepository.save(cliente);

        //Pesquisar cliente salvo por email
        var clienteEncontrado = this._customerRepository.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        assertThat(clienteEncontrado.getName()).isEqualTo(cliente.getName());
        assertThat(clienteEncontrado.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteEncontrado.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void updateTest() {
        var cliente = Customer.builder()
                .cpf("12345678912")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        var clienteSalvo = _customerRepository.save(cliente);

        //Pesquisar cliente salvo por ID
        var clienteEncontrado = _customerRepository.findById(clienteSalvo.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Atualizar campos
        clienteEncontrado.setName("Nome Atualizado");
        clienteEncontrado.setCpf("98765432100");

        // Salvar novamente
        var clienteAtualizado = _customerRepository.save(clienteEncontrado);

        // Verificar se foi atualizado
        assertThat(clienteAtualizado.getName()).isEqualTo("Nome Atualizado");
        assertThat(clienteAtualizado.getCpf()).isEqualTo("98765432100");
    }

    @Test
    void deleteTest() {
        var cliente = Customer.builder()
                .cpf("12345678912")
                .name("Joao da Silva")
                .email("joao@gmail.com")
                .build();

        var clienteSalvo = this._customerRepository.save(cliente);

        // Verificar se existe no banco de dados
        assertThat(this._customerRepository.findById(clienteSalvo.getId())).isNotEmpty();
        //delete o cliente
        this._customerRepository.delete(clienteSalvo);
        //verifica se existe no banco de dados
        assertThat(this._customerRepository.findById(clienteSalvo.getId())).isEmpty();

    }
}
