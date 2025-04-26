package br.com.fiap.controlepedidos.domain.repository;

import br.com.fiap.controlepedidos.domain.model.Cliente;
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

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void setup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void saveTest() {
        var cliente = Cliente.builder()
                .cpf("12345678912")
                .nome("Joao da Silva")
                .email("joao@gmail.com")
                .build();

        //Salvar novo cliente
        var clienteSalvo = this.clienteRepository.save(cliente);
        var clienteSalvo1 = this.clienteRepository.save(cliente);

        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getNome()).isEqualTo(cliente.getNome());
        assertThat(clienteSalvo.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteSalvo.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void findByIdTest() {
        var cliente = Cliente.builder()
                .cpf("12345678912")
                .nome("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        var clienteSalvo = this.clienteRepository.save(cliente);

        // Salvar cliente salvo por ID.
        Cliente clienteEncontrado = this.clienteRepository.findById(clienteSalvo.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        assertThat(clienteEncontrado.getNome()).isEqualTo(cliente.getNome());
        assertThat(clienteEncontrado.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteEncontrado.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void findByEmailTest() {
        var cliente = Cliente.builder()
                .cpf("12345678912")
                .nome("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        this.clienteRepository.save(cliente);

        //Pesquisar cliente salvo por email
        var clienteEncontrado = this.clienteRepository.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        assertThat(clienteEncontrado.getNome()).isEqualTo(cliente.getNome());
        assertThat(clienteEncontrado.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(clienteEncontrado.getCpf()).isEqualTo(cliente.getCpf());
    }

    @Test
    void updateTest() {
        var cliente = Cliente.builder()
                .cpf("12345678912")
                .nome("Joao da Silva")
                .email("joao@gmail.com")
                .build();
        // Salvar novo cliente
        var clienteSalvo = clienteRepository.save(cliente);

        //Pesquisar cliente salvo por ID
        var clienteEncontrado = clienteRepository.findById(clienteSalvo.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Atualizar campos
        clienteEncontrado.setNome("Nome Atualizado");
        clienteEncontrado.setCpf("98765432100");

        // Salvar novamente
        var clienteAtualizado = clienteRepository.save(clienteEncontrado);

        // Verificar se foi atualizado
        assertThat(clienteAtualizado.getNome()).isEqualTo("Nome Atualizado");
        assertThat(clienteAtualizado.getCpf()).isEqualTo("98765432100");
    }

    @Test
    void deleteTest() {
        var cliente = Cliente.builder()
                .cpf("12345678912")
                .nome("Joao da Silva")
                .email("joao@gmail.com")
                .build();

        var clienteSalvo = this.clienteRepository.save(cliente);

        // Verificar se existe no banco de dados
        assertThat(this.clienteRepository.findById(clienteSalvo.getId())).isNotEmpty();
        //delete o cliente
        this.clienteRepository.delete(clienteSalvo);
        //verifica se existe no banco de dados
        assertThat(this.clienteRepository.findById(clienteSalvo.getId())).isEmpty();

    }
}
