# 🚀 Mini Guia – Structurizr Lite no Windows
## 1. Verificar se você tem Java

O Structurizr Lite precisa do Java 11+ instalado.
Abra o Prompt de Comando (Win + R → cmd)

Digite:

``` shell
java -version
```

Se aparecer algo como java version "17.x" ou superior, está pronto.
Se não tiver, baixe o Java 17 LTS
 e instale.

## 2. Baixar o Structurizr Lite

Baixe o arquivo structurizr-lite.war (é só um arquivo único).

https://github.com/structurizr/lite/releases/download/v2025.05.28/structurizr-lite.war

## 3. Coloquei o arquivo .war na pasta c4model

Coloque o arquivo structurizr-lite.war dentro do diretório c4model
Estrutura da pasta deve ficar assim:

```
fiap_arquitetura_de_software
    controle-pedidos-fiap
        documentation
            c4model
                ├── structurizr-lite.war 
                └── workspace.dsl 
```

## 4. Rodar o Structurizr Lite

No terminal, entre na pasta:

````shell
cd documentation\c4model
````

E rode:

````shell
java -jar structurizr-lite.war .
````

## 5. Abrir no navegador

Abra o navegador e acesse:
👉 http://localhost:8080

Você verá a interface do Structurizr Lite.
Ele vai automaticamente carregar o workspace.dsl que está na pasta.

## 6. Visualizar e editar

Se você abrir o workspace.dsl no VS Code (com a extensão Structurizr instalada), terá syntax highlight e autocomplete.
Ao salvar o arquivo no VS Code, basta atualizar o navegador que o Structurizr Lite recarrega os diagramas.