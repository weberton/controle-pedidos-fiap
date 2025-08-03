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

## Implantação na Nuvem com Terraform e AWS
Além de rodar localmente ou com Docker, agora você pode subir toda a infraestrutura do projeto na AWS usando Terraform e Kubernetes.

### Pré-requisitos
Antes de começar, você vai precisar:

- [Terraform instalado](https://developer.hashicorp.com/terraform/install)
- [Conta na AWS](https://aws.amazon.com/)
- [AWS CLI instalado](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)

1. Criar suas credenciais na AWS
   - Acesse o Console da AWS → vá em IAM. 
   - Crie um usuário com acesso programático. 
   - Dê permissões de administrador ou permissões específicas para EC2, EKS, VPC, etc. 
   - Após a criação, anote o Access Key ID e o Secret Access Key.
   - Substitua o usuário no arquivo [variables](https://github.com/weberton/controle-pedidos-fiap/blob/main/terraform/aws/variables.tf), variable *eks_admin_principal_arn* 
   
2. Configurar AWS CLI
```
aws configure
```
  - Digite suas credenciais, região (ex: us-east-1) e formato de saída (json ou table).

3. Inicializar e aplicar Terraform
   - Acess o diretório *terraform*
   - Execute os comandos abaixo
   ```
   terraform init
   terraform plan
   terraform apply
   ```
   Isso vai provisionar toda a infraestrutura necessária: VPC, EKS (Kubernetes), e recursos relacionados.

### Deploy da Aplicação com Kubernetes
1. Conectar-se ao cluster EKS
```
aws eks update-kubeconfig --name controlepedidos-cluster --region us-east-1
```
2. Criar o namespace
   - Todos os recursos deste projeto são criados dentro do namespace controle-pedidos. Crie esse namespace com:
  ```
  kubectl create namespace controle-pedidos

  ```
3. Aplicar os arquivos do Kubernetes
   - Acesse o diretório k8s
   - O usuário e senha do BD podem ser trocados no arquivo mysql-secret.yaml(caso necessário)**[Optional]**
   - Todos os arquivos já especificam o namespace controle-pedidos, então você não precisa adicioná-lo manualmente nos comandos.
   - Com o cluster EKS pronto e configurado no seu kubectl, aplique os arquivos na ordem:
  ```
kubectl apply -f mysql-secret.yaml
kubectl apply -f app-configmap.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-hpa.yaml
kubectl apply -f loadbalancer.yaml
  ```
4. Acessar a aplicação via LoadBalancer
   - Após aplicar o loadbalancer.yml, execute:
   ```
     kubectl get svc controlepedidos -n controle-pedidos
   ```
   - Você verá algo como:
   ```
   NAME              TYPE           CLUSTER-IP     EXTERNAL-IP                                                              PORT(S)        AGE
   controlepedidos   LoadBalancer   172.20.60.85   a4a91eea98d404b2bafe383e838fdaf0-529162874.us-east-1.elb.amazonaws.com   80:30080/TCP   3m20s

   ```
   - Acesse a aplicação
   ```
   curl --location 'http://a4a91eea98d404b2bafe383e838fdaf0-529162874.us-east-1.elb.amazonaws.com/api/v1/customers'
   ```
   - Substitua o IP externo
   - **O loadbalancer pode demorar alguns minutos para ficar disponível** 
   
5. Rollout e Logs
   - Reiniciar o deploy (rollout)
   ```
   kubectl rollout restart deployment controlepedidos -n controle-pedidos
   ```
   - Ver os logs da aplicação
   ```
   kubectl logs deployment/controlepedidos -n controle-pedidos -f
   ```
   - 

#### Destruindo os Recursos (Cleanup)
Se você quiser remover todos os recursos criados para liberar espaço ou evitar cobranças na AWS, siga os passos abaixo.

1. Deletar recursos do Kubernetes
   - A forma mais simples e eficiente de remover todos os recursos criados é deletando o namespace controle-pedidos:
   ```
   kubectl delete namespace controle-pedidos
   ```
   - Isso irá remover:
     - Deployments (aplicação e MySQL)
     - Services (incluindo LoadBalancer)
     - Secrets e ConfigMaps 
     - PVCs e pods associados 
     - ⚠️ Esse comando não remove o cluster EKS, apenas os recursos que foram aplicados dentro do namespace.
     
2. Destruir infraestrutura na AWS com Terraform
Se você criou a infraestrutura usando Terraform (EKS, VPC, etc), volte para o diretório onde estão os arquivos .tf e execute:
```
terraform destroy
```
Esse comando irá:
- Apagar o cluster EKS 
- Remover a VPC, subnets, internet gateways, etc 
- Liberar os recursos alocados na sua conta AWS
  
**Importante**: certifique-se de que você realmente quer apagar tudo. Essa operação é irreversível.
3. 

