# Justificativa Técnica para a Escolha do Banco de Dados MySQL

## 1. Introdução

A escolha do sistema de gerenciamento de banco de dados (SGBD) é um fator determinante para a confiabilidade, desempenho e escalabilidade de uma aplicação.  
No contexto do projeto desenvolvido — um **sistema de pedidos para restaurante operado por totens locais** — optou-se pela utilização do **MySQL** como banco de dados relacional.  
A seguir, são apresentadas as justificativas técnicas que embasaram essa decisão.

---

## 2. Desempenho e eficiência para sistemas transacionais

O MySQL oferece excelente desempenho em ambientes de **processamento de transações online (OLTP)**, característica essencial para aplicações que realizam operações frequentes de leitura e escrita, como pedidos, pagamentos e atualizações de status.

Por meio do mecanismo de armazenamento **InnoDB**, o MySQL proporciona:
- **Bloqueio em nível de linha (row-level locking)**, reduzindo contenção entre transações concorrentes;
- **Controle de concorrência multiversão (MVCC)**, garantindo consistência dos dados mesmo com múltiplos acessos simultâneos;
- **Transações com propriedades ACID**, assegurando que operações críticas, como a criação de pedidos e processamento de pagamentos, sejam realizadas de forma confiável e atômica.

Essas características tornam o MySQL altamente adequado para o cenário de um restaurante, em que diversas requisições são realizadas em tempo real por diferentes totens.

---

## 3. Integridade referencial e consistência dos dados

A modelagem do banco de dados proposta faz uso extensivo de **chaves estrangeiras**, **restrições de integridade** e **índices** para manter a consistência entre entidades como `customers`, `orders`, `carts` e `payments`.

O suporte nativo do MySQL a essas funcionalidades garante:
- **Integridade referencial automática**, assegurando que exclusões ou atualizações em cascata sejam refletidas corretamente nas tabelas relacionadas;
- **Validação de regras de negócio** por meio de restrições `CHECK`, evitando registros inconsistentes (por exemplo, `quantity >= 1`);
- **Manutenção automática de relacionamentos** com cláusulas como `ON DELETE CASCADE` e `ON DELETE SET NULL`.

Esse conjunto de recursos é essencial para aplicações em que a confiabilidade das informações é um requisito fundamental.

---

## 4. Escalabilidade e simplicidade de implantação local

Por tratar-se de um sistema que opera localmente em totens dentro de um restaurante, o MySQL apresenta vantagens práticas significativas:
- **Instalação simples e baixo consumo de recursos**, permitindo sua execução em servidores locais de pequeno porte;
- **Replicação nativa** (Master-Slave ou Master-Master), possibilitando a sincronização de dados entre múltiplos totens ou com um servidor central;
- **Ferramentas de backup e recuperação nativas**, como `mysqldump` e `mysqlpump`, que facilitam a manutenção do sistema.

Essas características favorecem a adoção do MySQL em ambientes controlados e com necessidade de disponibilidade contínua.

---

## 5. Compatibilidade com o ecossistema de desenvolvimento

O MySQL possui **amplo suporte** em frameworks e linguagens de programação amplamente utilizadas, como **Java** e **Spring Boot**.  
Entre os principais benefícios, destacam-se:
- Drivers JDBC estáveis e de fácil configuração;
- Integração direta com **ORMs** como Hibernate e JPA, simplificando o mapeamento objeto-relacional das entidades do sistema;
- Suporte nativo a **pool de conexões** eficientes, como o HikariCP.

Essa integração reduz a complexidade do desenvolvimento e aumenta a produtividade da equipe.

---

## 6. Suporte a índices e otimização de consultas

O uso de **índices em colunas estratégicas** (como `cpf`, `cart_id` e `product_id`) e de **tipos binários para identificadores UUID (`BINARY(16)`)** é bem suportado pelo MySQL.  
Com isso, é possível obter:
- **Consultas mais rápidas** e com menor custo de leitura;
- **Planos de execução otimizados**, analisáveis via comando `EXPLAIN`;
- **Melhor aproveitamento de cache** e otimização de operações de junção (`JOIN`).

Esses recursos resultam em maior desempenho e menor latência nas consultas de pedidos, produtos e pagamentos.

---

## 7. Confiabilidade, maturidade e comunidade

O MySQL é um dos bancos de dados relacionais mais utilizados no mundo, amplamente testado e mantido pela Oracle.  
Sua longa trajetória garante:
- **Estabilidade comprovada em produção**;
- **Atualizações contínuas de segurança e desempenho**;
- **Amplo suporte comunitário e documentação abrangente**, facilitando o aprendizado e a resolução de problemas.

Esses fatores tornam o MySQL uma escolha segura para projetos de médio e grande porte.

---

## 8. Custo-benefício

Por ser uma solução **open source**, o MySQL oferece **baixo custo de implantação** e **licenciamento gratuito** sob a licença GPL.  
Essa característica reduz o custo total de propriedade e torna o SGBD ideal para sistemas locais ou de pequena escala, como o de um restaurante com totens de autoatendimento.

---

## 9. Conclusão

A adoção do **MySQL** como banco de dados para o sistema de pedidos do restaurante é tecnicamente justificada pelos seguintes fatores:

- Desempenho otimizado para transações frequentes (OLTP);
- Garantias de integridade e consistência dos dados;
- Facilidade de implantação e manutenção local;
- Integração nativa com frameworks Java e Spring Boot;
- Suporte a índices, otimização de consultas e replicação;
- Confiabilidade, maturidade e excelente custo-benefício.

Esses aspectos consolidam o MySQL como uma solução robusta, eficiente e alinhada às necessidades técnicas e operacionais do projeto.

---
