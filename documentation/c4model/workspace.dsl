workspace "Controle de Pedidos" "Sistema de e-commerce para gerenciar clientes, carrinhos, pedidos e pagamentos." {

    model {
        // Pessoas
        user = person "Cliente" "Usuário que realiza pedidos e interage via API REST."

        // Sistemas externos
        mercadopago = softwareSystem "MercadoPago" "Sistema externo de pagamentos." {
            tags "External"
        }

        // Sistema principal
        system = softwareSystem "Controle de Pedidos" {

            // Containers
            api = container "API REST" {
                technology "Spring Boot"
                description "Expõe os endpoints da aplicação para os clientes."
                tags "API"

                // Components API
                cartsController = component "CartsController" "Gerencia operações do carrinho (criar, adicionar item, etc)." {
                    tags "Controller"
                    technology "Spring MVC"
                }
                checkoutController = component "CheckoutController" "Processa o checkout e o pagamento." {
                    tags "Controller"
                    technology "Spring MVC"
                }
                customerController = component "CustomerController" "Gerencia os dados do cliente." {
                    tags "Controller"
                    technology "Spring MVC"
                }
                ordersController = component "OrdersController" "Gerencia o status e consulta de pedidos." {
                    tags "Controller"
                    technology "Spring MVC"
                }
                productController = component "ProductController" "Permite a consulta de produtos." {
                    tags "Controller"
                    technology "Spring MVC"
                }
            }

            services = container "Camada de Serviços" {
                technology "Spring Components"
                description "Contém a lógica de negócio da aplicação."
                tags "Service Layer"

                cartService = component "Cart Service" "Lógica de negócio para carrinhos." {
                    tags "Service"
                    technology "Spring Service"
                }
                checkoutService = component "Checkout Service" "Orquestra o processo de pagamento e criação de pedido." {
                    tags "Service"
                    technology "Spring Service"
                }
                customerService = component "Customer Service" "Lógica de negócio para clientes." {
                    tags "Service"
                    technology "Spring Service"
                }
                orderService = component "Order Service" "Lógica de negócio para pedidos." {
                    tags "Service"
                    technology "Spring Service"
                }
                productService = component "Product Service" "Lógica de negócio para produtos." {
                    tags "Service"
                    technology "Spring Service"
                }
            }

            domain = container "Domínio" {
                technology "Java"
                description "Modelo de domínio com entidades, enums e exceções."
                tags "Domain Layer"

                entities = component "Entities" "Classes de domínio (Cart, Customer, Order, Product, Payment)." {
                    technology "Java Classes"
                    tags "Domain"
                }
                enums = component "Enums" "Tipos enumerados (OrderStatus, PaymentStatus)." {
                    technology "Java Enums"
                    tags "Domain"
                }
                validations = component "Validations" "Exceções e regras de negócio." {
                    technology "Java Classes"
                    tags "Domain"
                }
            }

            repository = container "Camada de Repositório" {
                technology "Spring Data JPA"
                description "Interfaces para abstração da persistência de dados e gateways."
                tags "Repository Layer"

                cartRepository = component "CartsRepository" "Interface para persistência de carrinhos." {
                    tags "Repository"
                    technology "Spring Repository"
                }
                customerRepository = component "CustomerRepository" "Interface para persistência de clientes." {
                    tags "Repository"
                    technology "Spring Repository"
                }
                orderRepository = component "IOrderRepository" "Interface para persistência de pedidos." {
                    tags "Repository"
                    technology "Spring Repository"
                }
                productRepository = component "ProductRepository" "Interface para persistência de produtos." {
                    tags "Repository"
                    technology "Spring Repository"
                }
                paymentGateway = component "IPaymentGateway" "Interface que abstrai o gateway de pagamento." {
                    tags "Gateway"
                    technology "Java Interface"
                }
            }

            mercadopagoAdapter = container "Adapter MercadoPago" {
                technology "REST Client"
                description "Implementação do gateway de pagamento que se comunica com a API do MercadoPago."
                tags "Adapter"

                mpClient = component "MercadoPagoPaymentClient" "Cliente HTTP que realiza as chamadas para o MercadoPago." {
                    tags "Adapter"
                    technology "HTTP Client"
                }
            }

            database = container "Banco de Dados" "Armazena todos os dados da aplicação" {
                technology "MySQL 8.0"
                tags "Database"
            }
        }

        // Relacionamentos
        user -> api "Faz requisições" "JSON/HTTPS"
        api -> services "Chama a lógica de negócio"
        services -> domain "Usa o modelo de domínio"
        services -> repository "Solicita dados e operações de persistência"
        repository -> database "Lê e escreve" "JDBC"
        services -> mercadopagoAdapter "Delega o processamento de pagamentos"
        mercadopagoAdapter -> mercadopago "Chama a API de pagamentos" "JSON/HTTPS"

        // API -> Services
        cartsController -> cartService "Usa"
        checkoutController -> checkoutService "Usa"
        customerController -> customerService "Usa"
        ordersController -> orderService "Usa"
        productController -> productService "Usa"

        // Services -> Repository / Gateway
        cartService -> cartRepository "Usa"
        customerService -> customerRepository "Usa"
        orderService -> orderRepository "Usa"
        productService -> productRepository "Usa"
        checkoutService -> orderRepository "Usa"
        checkoutService -> paymentGateway "Usa"

        // Adapter -> Gateway Implementation
        paymentGateway -> mpClient "É implementado por"
    }

    // Views
    views {
        systemContext system "ContextoDoSistema" {
            include *
            autoLayout
            title "C1 - Diagrama de Contexto do Sistema 'Controle de Pedidos'"
        }

        container system "Containers" {
            include *
            autoLayout
            title "C2 - Diagrama de Containers do Sistema 'Controle de Pedidos'"
        }

        component api "ComponentesAPI" {
            include *
            autoLayout
            title "C3 - Componentes do Container 'API REST'"
        }

        component services "ComponentesServicos" {
            include *
            autoLayout
            title "C3 - Componentes do Container 'Serviços'"
        }

        component repository "ComponentesRepositorio" {
            include *
            autoLayout
            title "C3 - Componentes do Container 'Repositório'"
        }

        theme "default"
    }
}
