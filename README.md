# 🧾 Customer Management API — SOAP

> API SOAP para gerenciamento de clientes, desenvolvida em Java puro com JAX-WS. Permite criar, consultar, alterar e excluir clientes, com dados mantidos em memória.

![Java](https://img.shields.io/badge/Java-22-orange?style=flat-square&logo=openjdk)
![JAX-WS](https://img.shields.io/badge/JAX--WS-SOAP-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat-square&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow?style=flat-square)

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura da Aplicação](#-arquitetura-da-aplicação)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Fluxo de Requisição](#-fluxo-de-requisição)
- [Operações Disponíveis](#-operações-disponíveis)
- [Exemplos de Requisições e Respostas](#-exemplos-de-requisições-e-respostas)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Como Rodar o Projeto](#-como-rodar-o-projeto)
- [Boas Práticas Adotadas](#-boas-práticas-adotadas)
- [Melhorias Futuras](#-melhorias-futuras)

---

## 📌 Sobre o Projeto

A **Customer Management API** é uma API SOAP desenvolvida com **Java 22** e **JAX-WS**, focada no gerenciamento de clientes. Ela oferece operações completas de CRUD — criação, consulta, alteração e exclusão — sem depender de banco de dados externo: todos os dados são armazenados em memória durante a execução da aplicação.

O serviço é publicado diretamente com `javax.xml.ws.Endpoint`, sem necessidade de servidor de aplicação externo como Tomcat ou WildFly.

**Endpoint do serviço:** `http://localhost:8090/cliente`  
**WSDL disponível em:** `http://localhost:8090/cliente?wsdl`

---

## 🏛️ Arquitetura da Aplicação

A aplicação segue uma arquitetura em camadas simples e coesa:

```
┌───────────────────────────────────────────────────┐
│               Cliente SOAP                         │
│   (SoapUI, Postman, outro sistema, etc.)           │
└───────────────────┬───────────────────────────────┘
                    │  Envelope SOAP (XML) via HTTP
                    ▼
┌───────────────────────────────────────────────────┐
│           Service Layer — ClienteService           │
│  @WebService / @WebMethod                          │
│  Expõe os métodos como operações SOAP.             │
│  Valida entradas e aplica as regras de negócio.    │
│  Retorna mensagens de sucesso ou erro como String. │
└───────────────────┬───────────────────────────────┘
                    │
                    ▼
┌───────────────────────────────────────────────────┐
│            DAO Layer — ClienteDao                  │
│  Responsável pela persistência em memória.         │
│  Mantém uma List<Cliente> e realiza as operações   │
│  de adicionar, consultar, alterar e excluir.       │
└───────────────────┬───────────────────────────────┘
                    │
                    ▼
┌───────────────────────────────────────────────────┐
│           Model Layer — Cliente                    │
│  Representa o domínio. Cada instância possui:      │
│  id (UUID), nome, email, cpf.                      │
└───────────────────────────────────────────────────┘
```

### Descrição das Camadas

| Camada | Classe | Responsabilidade |
|--------|--------|-----------------|
| **Service** | `ClienteService` | Expõe as operações SOAP, valida parâmetros e coordena a lógica de negócio |
| **DAO** | `ClienteDao` | Persiste e recupera objetos `Cliente` de uma `List` em memória |
| **Model** | `Cliente` | Representa a entidade de domínio com seus atributos e um UUID gerado automaticamente |
| **Main** | `Main` | Publica o serviço SOAP no endpoint configurado via `Endpoint.publish()` |

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 22 | Linguagem principal |
| JAX-WS | (built-in JDK) | Framework para criação e publicação do serviço SOAP |
| `javax.xml.ws.Endpoint` | (built-in JDK) | Publicação do serviço sem servidor de aplicação externo |
| Maven | 3.x | Gerenciamento de dependências e build |
| UUID | (built-in JDK) | Geração de identificadores únicos para cada cliente |

---

## 🔄 Fluxo de Requisição

Todas as operações seguem o mesmo fluxo geral:

```
Cliente SOAP (SoapUI / sistema externo)
        │
        │  HTTP POST com Envelope SOAP (XML)
        │  para: http://localhost:8090/cliente
        ▼
ClienteService (@WebService)
        │
        │  1. Valida se os parâmetros são nulos ou em branco
        │     └─► Retorna mensagem de erro se inválidos
        │
        │  2. Delega operação ao ClienteDao
        │
        │  3. Confirma resultado (consulta pós-operação)
        │     └─► Retorna mensagem de sucesso ou erro
        ▼
ClienteDao
        │
        │  Manipula a List<Cliente> em memória
        │  (adicionar / buscar / alterar / remover)
        ▼
Resposta SOAP (XML) retornada ao cliente
```

---

## 📡 Operações Disponíveis

O serviço expõe quatro operações via SOAP:

| Operação | Parâmetros | Retorno |
|----------|------------|---------|
| `criarCliente` | `nome`, `email`, `cpf` | Mensagem de sucesso ou erro |
| `consultaCliente` | `email` | Mensagem indicando se o cliente existe |
| `excluirCliente` | `email` | Mensagem de sucesso ou erro |
| `alterarDadosCliente` | `nome`, `email`, `cpf` | Mensagem de sucesso ou erro |

> Todas as operações retornam uma `String` com a mensagem de resultado.

---

## 📨 Exemplos de Requisições e Respostas

Os exemplos abaixo usam o formato de envelope SOAP. Você pode testá-los via **SoapUI**, **Postman** (com suporte a SOAP) ou qualquer cliente HTTP.

**URL do endpoint:** `http://localhost:8090/cliente`  
**Content-Type:** `text/xml`

---

### ➕ Criar Cliente

**Requisição:**
```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service.example.org/">
  <soapenv:Header/>
  <soapenv:Body>
    <ser:criarCliente>
      <nome>João Silva</nome>
      <email>joao.silva@email.com</email>
      <cpf>123.456.789-00</cpf>
    </ser:criarCliente>
  </soapenv:Body>
</soapenv:Envelope>
```

**Resposta — sucesso:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:criarClienteResponse xmlns:ns2="http://service.example.org/">
      <return>Cliente criado com sucesso</return>
    </ns2:criarClienteResponse>
  </S:Body>
</S:Envelope>
```

**Resposta — campos em branco:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:criarClienteResponse xmlns:ns2="http://service.example.org/">
      <return>Coloque todos os valores para criar um cliente!</return>
    </ns2:criarClienteResponse>
  </S:Body>
</S:Envelope>
```

---

### 🔍 Consultar Cliente

**Requisição:**
```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service.example.org/">
  <soapenv:Header/>
  <soapenv:Body>
    <ser:consultaCliente>
      <email>joao.silva@email.com</email>
    </ser:consultaCliente>
  </soapenv:Body>
</soapenv:Envelope>
```

**Resposta — cliente encontrado:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:consultaClienteResponse xmlns:ns2="http://service.example.org/">
      <return>Cliente encontrado com sucesso</return>
    </ns2:consultaClienteResponse>
  </S:Body>
</S:Envelope>
```

**Resposta — cliente não encontrado:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:consultaClienteResponse xmlns:ns2="http://service.example.org/">
      <return>O cliente não foi encontrado!</return>
    </ns2:consultaClienteResponse>
  </S:Body>
</S:Envelope>
```

---

### ✏️ Alterar Dados do Cliente

**Requisição:**
```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service.example.org/">
  <soapenv:Header/>
  <soapenv:Body>
    <ser:alterarDadosCliente>
      <nome>João Souza</nome>
      <email>joao.souza@email.com</email>
      <cpf>123.456.789-00</cpf>
    </ser:alterarDadosCliente>
  </soapenv:Body>
</soapenv:Envelope>
```

**Resposta — sucesso:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:alterarDadosClienteResponse xmlns:ns2="http://service.example.org/">
      <return>Os dados foram alterados corretamente!</return>
    </ns2:alterarDadosClienteResponse>
  </S:Body>
</S:Envelope>
```

**Resposta — cliente não encontrado:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:alterarDadosClienteResponse xmlns:ns2="http://service.example.org/">
      <return>O cliente não foi encontrado, informe um cliente cadastrado!</return>
    </ns2:alterarDadosClienteResponse>
  </S:Body>
</S:Envelope>
```

---

### 🗑️ Excluir Cliente

**Requisição:**
```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service.example.org/">
  <soapenv:Header/>
  <soapenv:Body>
    <ser:excluirCliente>
      <email>joao.silva@email.com</email>
    </ser:excluirCliente>
  </soapenv:Body>
</soapenv:Envelope>
```

**Resposta — sucesso:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:excluirClienteResponse xmlns:ns2="http://service.example.org/">
      <return>O cliente foi removido!</return>
    </ns2:excluirClienteResponse>
  </S:Body>
</S:Envelope>
```

**Resposta — cliente não cadastrado:**
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
  <S:Body>
    <ns2:excluirClienteResponse xmlns:ns2="http://service.example.org/">
      <return>O cliente não foi excluido, informe um email cadastrado!</return>
    </ns2:excluirClienteResponse>
  </S:Body>
</S:Envelope>
```

---

## 📁 Estrutura de Pastas

```
cliente-soap-api/
├── src/
│   └── main/
│       └── java/
│           └── org/example/
│               ├── Main.java                  # Publica o serviço via Endpoint.publish()
│               │
│               ├── service/
│               │   └── ClienteService.java    # @WebService — operações SOAP expostas
│               │
│               ├── Dao/
│               │   └── ClienteDao.java        # Persistência em memória (List<Cliente>)
│               │
│               └── model/
│                   └── Cliente.java           # Entidade de domínio (id, nome, email, cpf)
│
├── pom.xml                                    # Dependências e configuração Maven
└── README.md
```

---

## ▶️ Como Rodar o Projeto

### Pré-requisitos

- [Java 22+](https://adoptium.net/) instalado e configurado no `PATH`
- [Maven 3.8+](https://maven.apache.org/) instalado

### 1. Clone o repositório

```bash
git clone https://github.com/davidcordeiro15/CP1_SOA.git
cd CP1_SOA
```

### 2. Compile o projeto

```bash
mvn clean compile
```

### 3. Execute a aplicação

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Ou, se preferir gerar e executar o JAR:

```bash
mvn clean package
java -cp target/cliente-soap-api-1.0.jar org.example.Main
```

Saída esperada no console:

```
Serviço SOAP rodando em: http://localhost:8090/cliente
```

### 4. Confirme que o serviço está no ar

Acesse no navegador ou via curl:

```bash
curl http://localhost:8090/cliente?wsdl
```

Se o WSDL for exibido em XML, o serviço está rodando corretamente. ✅

### 5. Teste com SoapUI

1. Abra o **SoapUI** (download em [soapui.org](https://www.soapui.org/))
2. Crie um novo projeto: **File → New SOAP Project**
3. Informe a URL do WSDL: `http://localhost:8090/cliente?wsdl`
4. O SoapUI gerará automaticamente os envelopes de requisição para cada operação
5. Preencha os parâmetros e clique em **▶ Run**

---

## ✅ Boas Práticas Adotadas

**Separação de responsabilidades em camadas**
A lógica de negócio fica no `ClienteService`, a persistência no `ClienteDao` e o domínio no `Cliente`. Nenhuma camada acessa diretamente a responsabilidade da outra de forma invertida.

**Identificação única com UUID**
Cada cliente recebe um `id` gerado automaticamente com `UUID.randomUUID()`, garantindo unicidade mesmo sem banco de dados.

**Validação de entrada no Service**
Todos os métodos verificam se os parâmetros recebidos são nulos ou em branco antes de prosseguir, retornando mensagens de erro descritivas ao invocador.

**Tratamento de exceções com try-catch**
As operações são envolvidas em blocos `try-catch` para capturar `RuntimeException` e retornar mensagens de erro sem derrubar o serviço.

**Publicação sem dependência de servidor externo**
O uso de `javax.xml.ws.Endpoint.publish()` elimina a necessidade de configurar Tomcat, WildFly ou qualquer contêiner de aplicação, simplificando o desenvolvimento e a execução local.

**Consulta pós-operação para confirmação**
Após criar ou alterar um cliente, o service realiza uma consulta para confirmar que a operação foi persistida corretamente antes de retornar sucesso.

---

## 🔮 Melhorias Futuras

| Melhoria | Descrição |
|----------|-----------|
| **Banco de dados** | Substituir a `List` em memória por JPA + H2 (embutido) ou PostgreSQL |
| **Validação de CPF** | Implementar validação do formato e dos dígitos verificadores do CPF |
| **E-mail duplicado** | Impedir criação de dois clientes com o mesmo e-mail |
| **Logging** | Adicionar SLF4J + Logback para rastreamento das operações |
| **Testes unitários** | Cobrir `ClienteService` e `ClienteDao` com JUnit 5 e Mockito |
| **Retorno estruturado** | Substituir retorno `String` por objetos de resposta serializados em XML com status e dados |
| **WS-Security** | Adicionar autenticação nos headers SOAP para proteger o serviço |
| **Migração Spring Boot** | Adotar `spring-boot-starter-web-services` para configuração mais robusta e escalável |
| **Docker** | Criar `Dockerfile` para containerização e fácil distribuição |

---

## 👤 Autor

Desenvolvido como projeto de portfólio demonstrando criação de uma API SOAP com Java puro e JAX-WS.

---

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).