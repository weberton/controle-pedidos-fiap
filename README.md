# Controle de Pedidos - Tech Challenge FIAP

Projeto backend para controle de pedidos de uma lanchonete, utilizando Spring Boot, Arquitetura Hexagonal e Flyway para migração de banco de dados.

![Cobertura de Código](.github/badges/jacoco.svg)
![Cobertura de Branches](.github/badges/branches.svg)

## Introdução

Esse projeto foi criando o intuito de criar um sistema de controle de pedidos para uma lanchonete, onde é possível que o cliente crie seus pedidos, realize o pagamento e acompanhe o status desse pedido até o passo final de retirada. Já o estabelecimento pode gerenciar os pedidos, gerenciar os produtos, podendo adicionar, remover ou editar os itens disponíveis para os clientes e também gerenciar campanhas de marketing para pessoas que se cadastraram com e-mail no sistema. A cozinha fica responsável pelo recebimento do pedido, confecção e atualizações dos status para que o cliente possa acompanhar.

## Pré-requisitos
- Java 21+
- MySQL 8+

## Iniciando projeto

### Rodando local

#### Configuração do Banco de Dados
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
#### Migração de Banco
As tabelas são gerenciadas automaticamente pelo Flyway.

Ao iniciar a aplicação, o Flyway aplica as migrations que estão em:
```shell
src/main/resources/db/migration
```
#### Rodando a aplicação
```shell
./mvnw spring-boot:run

```

### Rodando com Docker
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

 O Flyway se encarrega de criar as tabelas automaticamente na inicialização da aplicação.

### 
 Variáveis de Ambiente
As variáveis de configuração, como URL, usuário e senha do banco de dados, estão centralizadas em um arquivo .env.
Este arquivo não é versionado por segurança. Crie um manualmente na raiz do projeto com o seguinte conteúdo de exemplo:

```shell
MYSQL_DB=controle_pedidos
MYSQL_USER=spring

SPRING_DB_URL=jdbc:mysql://mysql:3306/controle_pedidos
SPRING_DB_USER=spring
SPRING_DB_PASS=spring

```

### Documentação swagger
A documentação em swagger com exemplos de requisições para testar a API está disponível em:

```shell
http://localhost:8080/swagger-ui/index.html
```


### Coleção Postman
A coleção Postman com exemplos de requisições para testar a API está disponível em:

```shell
postman/controle-pedidos.postman_collection.json
```

## Me
Help me update the readme file.

I want to add a section where I explain the github actions/workflow. Mention that the PR have checks, test should and sonar is enableed

Also the folder bagde with test coverage

## AI Assistant
I'll help you update the README.md file by adding a section about GitHub Actions/workflow and clarifying the badges. Here's the suggested addition to include after the badges section and before the "Introdução" section:

```markdown
## CI/CD & Code Quality

### GitHub Actions
Este projeto utiliza GitHub Actions para garantir a qualidade e integridade do código. Todos os Pull Requests passam pelos seguintes checks automatizados:

- **Testes Unitários**: Execução automática de todos os testes para garantir que novas mudanças não quebrem funcionalidades existentes
- **Cobertura de Código**: Análise de cobertura de testes usando JaCoCo, visualizada através dos badges acima
- **Análise Sonar**: Verificação de qualidade de código através do SonarCloud, incluindo:
  - Code smells
  - Vulnerabilidades
  - Débitos técnicos
  - Duplicação de código
  - Complexidade ciclomática

### Badges
Os badges no topo do README são atualizados automaticamente e mostram:
- ![Cobertura de Código](.github/badges/jacoco.svg) - Porcentagem total de cobertura de código
- ![Cobertura de Branches](.github/badges/branches.svg) - Porcentagem de cobertura de branches

Estes badges são gerados automaticamente após cada execução bem-sucedida do workflow de CI/CD e refletem o estado atual do projeto.
```

