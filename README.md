# Controle de Pedidos - Tech Challenge FIAP

Projeto backend para controle de pedidos de uma lanchonete, utilizando Spring Boot, Arquitetura Hexagonal e Flyway para migração de banco de dados.

## Pré-requisitos
- Java 21+
- MySQL 8+

## Configuração do Banco de Dados
1) Criar o banco de dados:
```sql
CREATE DATABASE controle_pedidos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2) Configurar o banco no arquivo application.properties:
```shell
spring.datasource.url=jdbc:mysql://localhost:3306/controle_pedidos
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA

```
### Migração de Banco
As tabelas são gerenciadas automaticamente pelo Flyway.

Ao iniciar a aplicação, o Flyway aplica as migrations que estão em:
```shell
src/main/resources/db/migration
```
### Rodando a aplicação
```shell
./mvnw spring-boot:run

```