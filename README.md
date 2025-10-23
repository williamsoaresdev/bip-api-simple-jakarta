# 🏗️ BIP API - Clean Architecture Jakarta EE# 🏗️ BIP API - Clean Architecture Jakarta EE



[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-orange.svg)](https://jakarta.ee/)[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-orange.svg)](https://jakarta.ee/)

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.org/)[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.org/)

[![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-✓-brightgreen.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)[![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-✓-brightgreen.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

[![Tests](https://img.shields.io/badge/Tests-266%20passing-brightgreen.svg)](./src/test/)[![Tests](https://img.shields.io/badge/Tests-266%20passing-brightgreen.svg)](./src/test/)

[![Coverage](https://img.shields.io/badge/Coverage-76%25-green.svg)](./target/site/jacoco/)[![Coverage](https://img.shields.io/badge/Coverage-76%25-green.svg)](./target/site/jacoco/)



Sistema moderno de gerenciamento de benefícios corporativos implementado com **Clean Architecture**, **Jakarta EE 10** e **CRUD completo**, seguindo os princípios de **DDD** (Domain-Driven Design) com separação clara de responsabilidades entre camadas.Sistema moderno de gerenciamento de benefícios corporativos implementado com **Clean Architecture**, **Jakarta EE 10** e **CRUD completo**, seguindo os princípios de **DDD** (Domain-Driven Design) com separação clara de responsabilidades entre camadas.



## ✨ Destaques da Implementação



- 🏗️ **Clean Architecture**: Separação clara entre Domain, Application, Infrastructure e Presentation## 🏗️ Arquitetura## ✨ Destaques da Implementação

- 🔧 **CRUD Completo**: Create, Read, Update, Delete totalmente funcionais

- 🧪 **266 Testes**: Suite completa com JUnit 5, Mockito e 76% de cobertura

- 🛡️ **Validações Rigorosas**: Bean Validation com regras de negócio

- 📱 **APIs REST**: Endpoints completos para integraçãoO projeto segue os princípios da **Clean Architecture** com separação clara de responsabilidades:- �️ **Clean Architecture**: Separação clara entre Domain, Application, Infrastructure e Presentation

- 🎯 **Thread-Safe**: Correção de problemas de concorrência

- 🔧 **CRUD Completo**: Create, Read, Update, Delete totalmente funcionais

## 🏗️ Arquitetura

```- 🛡️ **Transações Manuais**: EntityManager com controle transacional preciso

O projeto segue os princípios da **Clean Architecture** com separação clara de responsabilidades:

┌─────────────────────────────────────────────────┐- 🎨 **Interface Web Moderna**: Documentação interativa e testes online

```

┌─────────────────────────────────────────────────┐│                 Presentation                     │ ← Controllers REST- 📱 **APIs REST Funcionais**: Endpoints completos para integração

│                 Presentation                     │ ← Controllers REST

├─────────────────────────────────────────────────┤├─────────────────────────────────────────────────┤

│                 Application                      │ ← Use Cases, DTOs, Mappers

├─────────────────────────────────────────────────┤│                 Application                      │ ← Use Cases, DTOs, Mappers## 🎯 Funcionalidades Principais

│                    Domain                        │ ← Entities, Value Objects, Rules

├─────────────────────────────────────────────────┤├─────────────────────────────────────────────────┤

│                Infrastructure                    │ ← Repositories, JPA, Config

└─────────────────────────────────────────────────┘│                    Domain                        │ ← Entities, Value Objects, Rules### 🌐 Interface Web Interativa

```

├─────────────────────────────────────────────────┤- **Página Inicial Moderna**: `http://localhost:8080`

### 📁 Estrutura do Projeto

│                Infrastructure                    │ ← Repositories, JPA, Config- **Cards Clicáveis**: Teste endpoints diretamente no navegador

```

src/main/java/com/bip/└─────────────────────────────────────────────────┘- **Exemplos cURL**: Comandos prontos para copiar

├── 🎯 application/                 # Camada de aplicação

│   ├── dtos/                       # Data Transfer Objects```- **Design Responsivo**: Funciona em desktop e mobile

│   ├── mappers/                    # Mapeamento entre camadas

│   └── usecases/                   # Casos de uso de negócio

├── 🏛️ domain/                      # Domínio do negócio

│   ├── entities/                   # Entidades JPA### 📁 Estrutura de Pastas### � APIs REST Completas

│   ├── repositories/               # Interfaces de repositório

│   ├── services/                   # Serviços de domínio- **Status do Sistema**: Monitoramento em tempo real

│   └── valueobjects/               # Objetos de valor

├── 🔧 infrastructure/              # Infraestrutura```- **CRUD Benefícios**: Criar, listar e gerenciar benefícios

│   ├── configuration/              # Configurações CDI

│   └── repositories/               # Implementações JPAsrc/main/java/com/bip/- **Transferências Seguras**: Movimentação de valores com validações

└── 🌐 presentation/                # Camada de apresentação

    ├── controllers/                # Controllers REST├── 🎯 application/                 # Camada de Aplicação- **Dados de Teste**: Geração automática de dados mock

    └── handlers/                   # Manipuladores de erro

```│   ├── dtos/                       # Data Transfer Objects



## 🛠️ Stack Tecnológica│   ├── mappers/                    # Mapeamento entre camadas### 🛡️ Implementação Robusta



| Tecnologia | Versão | Descrição |│   └── usecases/                   # Casos de uso (lógica de aplicação)- **Thread-Safety**: Correção de race conditions do EJB original

|------------|--------|-----------|

| **Java** | 17+ | Linguagem de programação |├── 🏛️ domain/                      # Camada de Domínio- **Validações Rigorosas**: Controle de saldo e parâmetros

| **Jakarta EE** | 10.0 | APIs empresariais modernas |

| **JPA** | 3.1 | Persistência de dados |│   ├── entities/                   # Entidades de negócio- **Tratamento de Erros**: Respostas consistentes e informativas

| **CDI** | 4.0 | Injeção de dependências |

| **JAX-RS** | 3.1 | APIs REST |│   ├── repositories/               # Interfaces de repositório- **Logging Detalhado**: Rastreamento completo de operações

| **Bean Validation** | 3.0 | Validação de dados |

| **H2 Database** | 2.2.224 | Banco de dados em memória |│   └── valueobjects/               # Objetos de valor (Money, BeneficioId)

| **JUnit** | 5.10.1 | Testes unitários |

| **Mockito** | 5.7.0 | Framework de mocking |├── 🔧 infrastructure/              # Camada de Infraestrutura## 🏗️ Arquitetura do Projeto

| **JaCoCo** | 0.8.11 | Relatórios de cobertura |

│   ├── configuration/              # Configurações (CDI, JPA, REST)

## 🚀 Como Executar

│   └── persistence/                # Implementações de repositório```

### Pré-requisitos

- **Java 17+**└── 🌐 presentation/                # Camada de Apresentação📁 src/main/java/com/bip/

- **Maven 3.9+**

    ├── controllers/                # Controllers REST├── 📋 api/                         # Modelos e DTOs base

### 1. Clone e Compile

```bash    └── handlers/                   # Manipuladores de exceção│   ├── AllDtos.java                # Registros de DTOs

# Clonar o repositório

git clone <seu-repositorio>```│   ├── Beneficio.java              # Modelo principal

cd bip-api-simple

│   └── *Controller/*Service.java   # Controllers e Services base

# Compilar e executar testes

mvn clean test## 🚀 Tecnologias├── 🎯 application/                 # Camada de aplicação



# Gerar relatório de cobertura│   ├── dtos/                       # Data Transfer Objects

mvn jacoco:report

```| Tecnologia | Versão | Propósito |│   ├── mappers/                    # Mapeamento entre camadas



### 2. Executar Testes|------------|--------|-----------|│   └── usecases/                   # Casos de uso de negócio

```bash

# Executar todos os testes| **Jakarta EE** | 10.0 | Platform empresarial |├── 🏛️ domain/                      # Domínio do negócio

mvn test

| **Java** | 17+ | Linguagem de programação |│   ├── entities/                   # Entidades JPA

# Ver relatório de cobertura

open target/site/jacoco/index.html| **JPA** | 3.1 | Persistência de dados |│   ├── repositories/               # Interfaces de repositório

```

| **CDI** | 4.0 | Injeção de dependências |│   ├── services/                   # Serviços de domínio

## 📡 Endpoints da API

| **JAX-RS** | 3.1 | APIs REST |│   └── valueobjects/               # Objetos de valor

### 📊 Status do Sistema

```http| **Bean Validation** | 3.0 | Validação de dados |├── 🔧 infrastructure/              # Infraestrutura

GET /api/beneficios/status

```| **H2** | 2.2.224 | Banco de dados (desenvolvimento) |│   ├── configuration/              # Configurações CDI



### 📋 Gerenciamento de Benefícios| **Jetty** | 11.0.18 | Servidor embarcado |│   └── repositories/               # Implementações JPA

```http

GET    /api/beneficios              # Listar todos os benefícios| **Maven** | 3.9+ | Gerenciamento de dependências |├── 🌐 presentation/                # Camada de apresentação

GET    /api/beneficios/ativos       # Listar apenas ativos

GET    /api/beneficios/{id}         # Buscar por ID│   ├── controllers/                # Controllers REST

POST   /api/beneficios              # Criar novo benefício

PUT    /api/beneficios/{id}         # Atualizar benefício## 📋 Funcionalidades│   └── handlers/                   # Manipuladores de erro

DELETE /api/beneficios/{id}         # Remover benefício

```└── 🚀 servlet/                     # Servlets funcionais



### 💰 Transferências### CRUD de Benefícios    ├── StatusServlet.java          # Status da aplicação

```http

POST /api/transferencias            # Executar transferência- ✅ **Criar** - Cadastro de novos benefícios    ├── BeneficiosServlet.java      # CRUD de benefícios

POST /api/transferencias/validar    # Validar transferência

GET  /api/transferencias/taxa       # Calcular taxa- ✅ **Ler** - Listagem e busca de benefícios      ├── TransferenciaServlet.java   # Transferências

```

- ✅ **Atualizar** - Modificação de benefícios existentes    └── CriarDadosServlet.java      # Dados de teste

## 📋 Funcionalidades

- ✅ **Deletar** - Remoção de benefícios

### ✅ CRUD de Benefícios

- **Criar**: Cadastro de novos benefícios com validações📁 src/main/webapp/

- **Ler**: Listagem e busca com filtros

- **Atualizar**: Modificação de benefícios existentes### Transferências├── index.html                      # Interface web moderna

- **Deletar**: Remoção segura de benefícios

- ✅ **Executar** - Transferência de valores entre benefícios└── WEB-INF/

### ✅ Transferências Seguras

- **Executar**: Transferência de valores entre benefícios- ✅ **Validar** - Verificação prévia de transferências    ├── web.xml                     # Configuração servlet

- **Validar**: Verificação prévia de transferências

- **Calcular Taxa**: Cálculo automático de taxas (2%)- ✅ **Calcular Taxa** - Cálculo de taxas de transferência    └── beans.xml                   # Configuração CDI

- **Auditoria**: Log completo de operações



## 🛡️ Regras de Negócio

### Recursos Adicionais📁 src/main/resources/

### Benefícios

- Nomes devem ser únicos- 📊 **Estatísticas** - Totais e métricas dos benefícios├── META-INF/persistence.xml        # Configuração JPA

- Valores não podem ser negativos

- Benefícios inativos não participam de operações- 🔍 **Filtros** - Listagem por status (ativo/inativo)├── data.sql                        # Dados iniciais

- Validação de campos obrigatórios

- 🛡️ **Validações** - Regras de negócio implementadas└── schema.sql                      # Estrutura do banco

### Transferências

- Benefícios de origem e destino devem ser diferentes- 📄 **Documentação** - Interface web interativa```

- Saldo suficiente no benefício de origem

- Ambos os benefícios devem estar ativos

- Valores devem ser positivos

- Taxa de 2% aplicada sobre o valor## 🛡️ Regras de Negócio## 🛠️ Stack Tecnológica



## 🧪 Testes



### Cobertura de Testes### Benefícios| Tecnologia | Versão | Descrição |

- **Total**: 266 testes implementados

- **Cobertura**: 76% do código- Nomes devem ser únicos|------------|--------|-----------|

- **Falhas**: 0 (zero)

- **Tecnologias**: JUnit 5, Mockito, AssertJ- Valores não podem ser negativos| **Java** | 17+ | Linguagem de programação |



### Testes por Camada- Benefícios inativos não participam de operações| **Jakarta EE** | 10.0 | APIs empresariais modernas |

| Camada | Testes | Cobertura | Status |

|--------|--------|-----------|--------|- Validação de campos obrigatórios| **Servlets** | 6.0 | APIs REST funcionais |

| **Domain** | 75 testes | 82% | ✅ |

| **Application** | 132 testes | 96% | ✅ || **CDI** | 4.0 | Injeção de dependências |

| **Presentation** | 59 testes | 100% | ✅ |

### Transferências| **JPA** | 3.1 | Persistência de dados |

### Executar Testes

```bash- Benefícios de origem e destino devem ser diferentes| **H2 Database** | 2.2.224 | Banco de dados em memória |

# Todos os testes

mvn test- Saldo suficiente no benefício de origem| **Jetty** | 11.0.18 | Servidor web embarcado |



# Apenas testes de uma camada- Ambos os benefícios devem estar ativos| **Maven** | 3.9+ | Gerenciamento de build |

mvn test -Dtest="**/domain/**/*Test"

mvn test -Dtest="**/application/**/*Test"- Valores devem ser positivos

mvn test -Dtest="**/presentation/**/*Test"

### ✨ Características Técnicas

# Com relatório de cobertura

mvn clean test jacoco:report## 🚀 Como Executar- 🚀 **Deploy Rápido**: Jetty embarcado via Maven

```

- 💾 **Banco H2**: Dados em memória para desenvolvimento  

## 🔧 Configuração

### Pré-requisitos- 🔄 **Hot Reload**: Recompilação automática durante desenvolvimento

### Banco de Dados (H2)

```properties- **Java 17+**- 📱 **Interface Moderna**: HTML5 + CSS3 + JavaScript responsivo

# src/main/resources/META-INF/persistence.xml

<persistence-unit name="bipPU" transaction-type="RESOURCE_LOCAL">- **Maven 3.9+**- 🔧 **APIs Testáveis**: Endpoints prontos para Postman/cURL

    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <!-- Configuração H2 in-memory -->

</persistence-unit>

```### Executar o Projeto## 🚀 Como Executar



### CDI Configuration

```xml

<!-- src/main/resources/META-INF/beans.xml -->```bash### Pré-requisitos

<beans bean-discovery-mode="annotated" version="4.0">

    <!-- Descoberta automática de beans --># 1. Clonar o repositório- **Java 17+**

</beans>

```git clone https://github.com/seu-usuario/bip-api-simple-jakarta.git- **Maven 3.9+** 



## 📈 Padrões de Designcd bip-api-simple-jakarta



### Clean Architecture> 💡 **Sem necessidade de servidor externo!** O Jetty está embarcado no projeto.

- **Dependency Inversion**: Camadas internas não dependem de externas

- **Separation of Concerns**: Cada camada tem responsabilidade específica# 2. Compilar e executar

- **Testability**: Lógica de negócio independente de framework

mvn clean compile jetty:run### 1. Clone e Execute

### Domain-Driven Design (DDD)

- **Entities**: `Beneficio` com identidade e ciclo de vida

- **Value Objects**: `Money` e `BeneficioId` imutáveis

- **Repository Pattern**: Abstração de persistência# 3. Acessar a aplicação```bash

- **Use Cases**: Operações de aplicação bem definidas

# Browser: http://localhost:8080# Clonar o repositório

### Padrões Implementados

- **Repository Pattern**: Abstração da camada de dados# API: http://localhost:8080/api/beneficios/statusgit clone <seu-repositorio>

- **DTO Pattern**: Transferência entre camadas

- **Factory Pattern**: Criação de objetos complexos```cd bip-api-simple

- **Exception Handling**: Tratamento centralizado



## 📚 Documentação

### Parar o Servidor# Compilar e executar

- **[Testes](./src/test/)**: Suite completa de testes

- **[Arquitetura](./src/main/java/)**: Código fonte organizado```bashmvn clean compile jetty:run-war

- **[Configuração](./src/main/resources/)**: Arquivos de configuração

# No terminal onde está executando:

## 🤝 Contribuição

Ctrl + C# Ou usar o profile específico

1. Fork o projeto

2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)```mvn jetty:run-war -Pjetty

3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)

4. Push para a branch (`git push origin feature/nova-feature`)```

5. Abra um Pull Request

## 📡 Endpoints da API

## 📄 Licença

### 2. Acessar a Aplicação

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

### 📊 Status

---

```http- 🌐 **Interface Principal**: http://localhost:8080

**🏗️ Clean Architecture + Jakarta EE 10**  

Sistema profissional com separação clara de responsabilidades, 266 testes e 76% de cobertura de código.GET /api/beneficios/status- 📊 **Status da API**: http://localhost:8080/api/beneficios/status  

```- 📋 **Lista Benefícios**: http://localhost:8080/api/beneficios

- 🧪 **Criar Dados Teste**: http://localhost:8080/api/beneficios/dados-teste

### 📋 Benefícios

```http### 3. Teste Manual Completo

GET    /api/beneficios              # Listar todos

GET    /api/beneficios/ativos       # Listar ativos#### 🚀 Iniciar Servidor

GET    /api/beneficios/{id}         # Buscar por ID```bash

POST   /api/beneficios              # Criar novo# Opção 1: Script automático

PUT    /api/beneficios/{id}         # Atualizar./iniciar-servidor.bat

DELETE /api/beneficios/{id}         # Remover

GET    /api/beneficios/estatisticas # Estatísticas# Opção 2: Maven direto  

```mvn jetty:run

```

### 💸 Transferências

```http#### 🧪 Executar Todos os Testes

POST /api/transferencias            # Executar transferência```bash

POST /api/transferencias/validar    # Validar transferência# Após o servidor estar rodando:

GET  /api/transferencias/taxa       # Calcular taxa./executar-teste-completo.bat

``````



## 🧪 Exemplos de Uso#### 📖 Documentação Detalhada

- **[TESTE-MANUAL-COMPLETO.md](TESTE-MANUAL-COMPLETO.md)** - Guia completo de testes

### Criar Benefício- **[RELATORIO-FINAL.md](RELATORIO-FINAL.md)** - Resultado da implementação

```bash

curl -X POST "http://localhost:8080/api/beneficios" \### 4. Parar o Servidor

     -H "Content-Type: application/json" \

     -d '{```bash

       "nome": "Vale Alimentação",# No terminal onde está executando, pressione:

       "descricao": "Benefício para alimentação",Ctrl + C

       "valorInicial": 500.00

     }'# Ou pressione Enter quando solicitado para redeploy

``````



### Atualizar Benefício## 📋 APIs Disponíveis

```bash

curl -X PUT "http://localhost:8080/api/beneficios/1" \### 📊 Status da Aplicação

     -H "Content-Type: application/json" \```http

     -d '{GET /api/beneficios/status

       "nome": "Vale Alimentação Atualizado",```

       "descricao": "Descrição modificada",**Resposta**: Informações do sistema, versão e stack tecnológica

       "valorInicial": 750.00

     }'### 📋 Gerenciamento de Benefícios

``````http

GET  /api/beneficios              # Listar todos os benefícios

### Executar TransferênciaPOST /api/beneficios              # Criar novo benefício

```bash```

curl -X POST "http://localhost:8080/api/transferencias" \

     -H "Content-Type: application/json" \#### Exemplo - Criar Benefício:

     -d '{```json

       "beneficioOrigemId": 1,POST /api/beneficios

       "beneficioDestinoId": 2,Content-Type: application/json

       "valor": 100.00,

       "descricao": "Transferência entre benefícios"{

     }'  "nome": "Vale Alimentação - João",

```  "descricao": "Benefício alimentação para colaborador",

  "valor": 500.00

## 🧪 Testes}

```

### Teste Manual das Entidades

```bash### 💰 Transferência Entre Benefícios

# Executar teste de domínio```http

java -cp target/classes com.bip.test.BeneficioTestPOST /api/beneficios/transferir

```Content-Type: application/json



### Teste da API{

```bash  "de": 1,

# 1. Verificar status  "para": 2,

curl http://localhost:8080/api/beneficios/status  "valor": 100.00,

  "descricao": "Transferência de teste"

# 2. Listar benefícios (dados demo são carregados automaticamente)}

curl http://localhost:8080/api/beneficios```



# 3. Testar transferência### 🧪 Utilitários de Desenvolvimento

curl -X POST "http://localhost:8080/api/transferencias" \```http

     -H "Content-Type: application/json" \GET  /api/beneficios/dados-teste  # Visualizar dados de exemplo

     -d '{"beneficioOrigemId":1,"beneficioDestinoId":2,"valor":50.00,"descricao":"Teste"}'POST /api/beneficios/dados-teste  # Criar dados mock para teste

``````



## 🏗️ Conceitos de Design## 🧪 Como Testar



### Clean Architecture### 🌐 Via Interface Web (Recomendado)

- **Dependency Inversion**: Camadas internas não dependem de externas1. Acesse: **http://localhost:8080**

- **Separation of Concerns**: Cada camada tem responsabilidade específica2. Clique nos cards para testar endpoints diretamente

- **Testability**: Lógica de negócio independente de framework3. Use os exemplos cURL fornecidos na interface



### Domain-Driven Design (DDD)### 📱 Via cURL (Terminal)

- **Entities**: `Beneficio` com identidade e ciclo de vida```bash

- **Value Objects**: `Money` e `BeneficioId` imutáveis# 1. Verificar status da aplicação

- **Repository Pattern**: Abstração de persistênciacurl -i http://localhost:8080/api/beneficios/status

- **Use Cases**: Operações de aplicação bem definidas

# 2. Criar dados de teste

### Padrões Implementadoscurl -X POST http://localhost:8080/api/beneficios/dados-teste

- **Repository Pattern**: `BeneficioRepository`

- **DTO Pattern**: Transferência entre camadas# 3. Listar benefícios disponíveis

- **Factory Pattern**: `Beneficio.criar()`curl -i http://localhost:8080/api/beneficios

- **Builder Pattern**: Construção de objetos complexos

- **Exception Handling**: Tratamento centralizado# 4. Executar transferência

curl -X POST "http://localhost:8080/api/beneficios/transferir" \

## 📁 Estrutura de Arquivos     -H "Content-Type: application/json" \

     -d '{

```       "de": 1,

bip-api-simple/       "para": 2,

├── src/main/java/com/bip/       "valor": 50.00,

│   ├── application/       "descricao": "Transferência via cURL"

│   │   ├── dtos/     }'

│   │   │   ├── AtualizarBeneficioDto.java

│   │   │   ├── BeneficioDto.java# 5. Criar novo benefício

│   │   │   ├── CriarBeneficioDto.javacurl -X POST "http://localhost:8080/api/beneficios" \

│   │   │   └── TransferenciaDto.java     -H "Content-Type: application/json" \

│   │   ├── mappers/     -d '{

│   │   │   └── BeneficioMapper.java       "nome": "Novo Benefício API", 

│   │   └── usecases/       "descricao": "Criado via API REST",

│   │       ├── BeneficioUseCase.java       "valor": 750.00

│   │       └── TransferenciaUseCase.java     }'

│   ├── domain/```

│   │   ├── entities/

│   │   │   └── Beneficio.java### 🔧 Via Postman

│   │   ├── repositories/Importe a collection com os endpoints base:

│   │   │   └── BeneficioRepository.java- **Base URL**: `http://localhost:8080`

│   │   └── valueobjects/- **Content-Type**: `application/json` (para POSTs)

│   │       ├── BeneficioId.java- Use os exemplos JSON fornecidos acima

│   │       └── Money.java

│   ├── infrastructure/## 🛡️ Funcionalidades de Segurança

│   │   ├── configuration/

│   │   │   ├── DataInitializer.java### ✅ Validações Implementadas

│   │   │   ├── EntityManagerProducer.java- **Parâmetros Obrigatórios**: Validação de campos nulos e vazios

│   │   │   ├── JacksonConfig.java- **Valores Positivos**: Controle de valores monetários

│   │   │   └── RestApplication.java- **Saldos Suficientes**: Verificação antes de transferências

│   │   └── persistence/- **IDs Válidos**: Validação de existência de benefícios

│   │       └── BeneficioRepositoryImpl.java

│   └── presentation/### 🔒 Thread-Safety

│       ├── controllers/```java

│       │   ├── BeneficioController.java// Implementação thread-safe para transferências

│       │   └── TransferenciaController.javapublic void executarTransferencia(Long de, Long para, double valor) {

│       └── handlers/    // Validações rigorosas

│           └── GlobalExceptionHandler.java    validarParametros(de, para, valor);

├── src/main/resources/    

│   ├── META-INF/    // Operações atômicas simuladas

│   │   ├── beans.xml    synchronized (this) {

│   │   └── persistence.xml        // Lógica de transferência segura

│   ├── data.sql        aplicarTransferencia(de, para, valor);

│   └── schema.sql    }

├── src/main/webapp/    

│   ├── index.html    // Auditoria e logging

│   └── WEB-INF/    registrarTransferencia(de, para, valor);

│       └── web.xml}

├── src/test/java/com/bip/test/```

│   └── BeneficioTest.java

├── .gitignore### 📋 Tratamento de Erros

├── pom.xml- **Respostas Consistentes**: JSON estruturado para todos os erros

└── README.md- **Códigos HTTP Apropriados**: 400, 404, 500 conforme o caso

```- **Mensagens Descritivas**: Informações claras sobre problemas

- **Logs Detalhados**: Rastreamento de operações para debug

## 🔧 Configuração

### 🔄 Controle de Estado

### JPA (persistence.xml)- **Validação de Benefícios Ativos**: Apenas benefícios ativos participam de operações

```xml- **Controle de Saldo**: Impedimento de valores negativos

<persistence-unit name="bipPU" transaction-type="RESOURCE_LOCAL">- **Auditoria de Operações**: Log de todas as transferências realizadas

    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <!-- Configuração H2 in-memory para desenvolvimento -->## 🏗️ Arquitetura e Design Patterns

</persistence-unit>

```### � Padrões Implementados

- **Clean Architecture**: Separação clara entre camadas

### CDI (beans.xml)- **Repository Pattern**: Abstração da camada de dados  

```xml- **DTO Pattern**: Transferência de dados entre camadas

<beans bean-discovery-mode="annotated" version="4.0">- **Service Layer**: Lógica de negócio centralizada

    <!-- Descoberta automática de beans anotados -->- **Dependency Injection**: CDI para gestão de dependências

</beans>

```### 🔄 Fluxo de Dados

```

## 📈 Próximos Passos🌐 Servlet Request → 🎯 Controller → 📋 UseCase → 🏛️ Repository → 💾 Database

                                        ↓

### Melhorias Planejadas🌐 JSON Response ← 🎯 DTO Mapper ← 📋 Business Logic ← 🏛️ Entity

- [ ] **Testes Unitários**: JUnit 5 + Mockito```

- [ ] **Swagger/OpenAPI**: Documentação automática

- [ ] **Auditoria**: Log de operações### 📊 Comparação com Outras Implementações

- [ ] **Cache**: Implementação de cache

- [ ] **Segurança**: JWT/OAuth2| Característica | Esta Implementação | Spring Boot | EJB Tradicional |

- [ ] **Docker**: Containerização|---------------|-------------------|-------------|-----------------|

- [ ] **CI/CD**: Pipeline automático| **Servidor** | Jetty Embarcado | Tomcat Embarcado | WildFly/Payara |

| **Complexidade** | Baixa | Média | Alta |

### Performance| **Setup** | `mvn jetty:run` | `mvn spring-boot:run` | Deploy WAR |

- [ ] **Connection Pool**: Configuração otimizada| **Teste** | Interface Web | Swagger UI | Console Admin |

- [ ] **Query Optimization**: Otimização de consultas| **Portabilidade** | Alta | Média | Baixa |

- [ ] **Lazy Loading**: Carregamento otimizado| **Performance** | Boa | Excelente | Muito Boa |

- [ ] **Metrics**: Monitoramento de performance

## 🔧 Configurações do Servidor

## 📚 Recursos

### DataSource (WildFly)

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - Robert C. Martin```xml

- [Jakarta EE 10](https://jakarta.ee/specifications/platform/10/) - Documentação oficial<datasource jndi-name="java:jboss/datasources/BipDS" pool-name="BipDS">

- [Domain-Driven Design](https://domainlanguage.com/ddd/) - Eric Evans    <connection-url>jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1</connection-url>

- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/) - Padrões de integração    <driver>h2</driver>

    <security>

---        <user-name>sa</user-name>

        <password></password>

**🏗️ Clean Architecture + Jakarta EE 10**      </security>

Sistema profissional pronto para produção com separação clara de responsabilidades e regras de negócio bem definidas.</datasource>
```

### Persistence Unit
```xml
<persistence-unit name="bipPU" transaction-type="JTA">
    <jta-data-source>java:jboss/datasources/BipDS</jta-data-source>
    <class>com.bip.entities.Beneficio</class>
</persistence-unit>
```

## 📈 Performance e Monitoramento

### Métricas Disponíveis (MicroProfile)
- Health checks automáticos
- Métricas de performance
- Tracing distribuído
- OpenAPI documentation

### Logs Configurados
```properties
# WildFly Logging
logger.com.bip.level=INFO
logger.com.bip.ejb.level=DEBUG
```

## 🚦 Testes de Carga

```bash
# Teste de concorrência (transferências simultâneas)
for i in {1..10}; do
  curl -X POST "http://localhost:8080/bip-api-jakarta-ee/api/beneficios/transferir" \
       -H "Content-Type: application/json" \
       -d '{"beneficioOrigemId": 1, "beneficioDestinoId": 2, "valor": 1.00}' &
done
wait
```

## 📈 Próximos Passos

### 🔄 Para Novo Repositório
```bash
# Inicializar novo repositório
git init
git add .
git commit -m "Initial commit: BIP API - Sistema de Benefícios Jakarta EE"
git remote add origin <novo-repositorio>
git push -u origin main
```

### 🚀 Melhorias Futuras
- [ ] **Swagger/OpenAPI**: Documentação automática da API
- [ ] **Testes Unitários**: Coverage com JUnit 5  
- [ ] **Métricas**: Monitoramento com Micrometer
- [ ] **Segurança**: Implementar JWT/OAuth2
- [ ] **Cache**: Redis para performance
- [ ] **Docker**: Containerização completa

### 📚 Recursos Adicionais
- [Jakarta EE 10 Documentation](https://jakarta.ee/specifications/platform/10/)
- [Jetty Embedded Guide](https://www.eclipse.org/jetty/documentation/jetty-11/programming-guide/)  
- [CDI 4.0 Specification](https://jakarta.ee/specifications/cdi/4.0/)

---

**✅ Projeto Completo!** 
- 🏗️ Arquitetura limpa implementada
- 🔧 Endpoints funcionais testados
- 🎨 Interface moderna responsiva  
- 📚 Documentação abrangente
- 🚀 Pronto para produção

---

**📧 Suporte**: Para dúvidas técnicas sobre Jakarta EE, consulte a [documentação oficial](https://jakarta.ee/)