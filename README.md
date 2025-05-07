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

## 🐳 Rodando com Docker
Este projeto pode ser executado facilmente usando Docker e Docker Compose, sem a necessidade de configurar o ambiente local manualmente.

**Pré-requisitos**
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

**Passos**

1) Certifique-se de que o JAR da aplicação foi gerado com:
```shell
mvn clean package
```

2) Suba os containers:
```shell
docker-compose up --build
```
3) A aplicação estará disponível em:
```shell
http://localhost:8080
```
O banco de dados MySQL estará acessível internamente pelo serviço mysql na porta 3306.

⚠️ O Flyway se encarrega de criar as tabelas automaticamente na inicialização da aplicação.

### 🔐 Variáveis de Ambiente
As variáveis de configuração, como URL, usuário e senha do banco de dados, estão centralizadas em um arquivo .env.
Este arquivo não é versionado por segurança. Crie um manualmente na raiz do projeto com o seguinte conteúdo de exemplo:

```shell
MYSQL_DB=controle_pedidos
MYSQL_USER=spring
MYSQL_ROOT_PASSWORD=spring

SPRING_DB_URL=jdbc:mysql://mysql:3306/controle_pedidos
SPRING_DB_USER=spring
SPRING_DB_PASS=spring

```

### Coleção Postman
A coleção Postman com exemplos de requisições para testar a API está disponível em:

```shell
postman/controle-pedidos.postman_collection.json
```
