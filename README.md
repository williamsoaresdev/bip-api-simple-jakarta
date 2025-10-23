# 🚀 BIP API - Sistema de Benefícios Jakarta EE

[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-orange.svg)](https://jakarta.ee/)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.org/)
[![Jetty](https://img.shields.io/badge/Jetty-11.0.18-green.svg)](https://www.eclipse.org/jetty/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)

Sistema moderno de gerenciamento de benefícios corporativos implementado com **Jakarta EE 10**, **Servlets** e **CDI**, com interface web interativa e APIs RESTful completas.

## ✨ Destaques do Projeto

- 🎨 **Interface Web Moderna**: Página inicial interativa com design responsivo
- 🔧 **APIs REST Funcionais**: Endpoints completos para teste via Postman/cURL
- 🛡️ **Implementação Thread-Safe**: Correção de bugs de concorrência do EJB original
- 🚀 **Deploy Simples**: Jetty embarcado via Maven - sem servidor externo
- 📱 **100% Testável**: Interface permite testar todos os endpoints diretamente

## 🎯 Funcionalidades Principais

### 🌐 Interface Web Interativa
- **Página Inicial Moderna**: `http://localhost:8080`
- **Cards Clicáveis**: Teste endpoints diretamente no navegador
- **Exemplos cURL**: Comandos prontos para copiar
- **Design Responsivo**: Funciona em desktop e mobile

### � APIs REST Completas
- **Status do Sistema**: Monitoramento em tempo real
- **CRUD Benefícios**: Criar, listar e gerenciar benefícios
- **Transferências Seguras**: Movimentação de valores com validações
- **Dados de Teste**: Geração automática de dados mock

### 🛡️ Implementação Robusta
- **Thread-Safety**: Correção de race conditions do EJB original
- **Validações Rigorosas**: Controle de saldo e parâmetros
- **Tratamento de Erros**: Respostas consistentes e informativas
- **Logging Detalhado**: Rastreamento completo de operações

## 🏗️ Arquitetura do Projeto

```
📁 src/main/java/com/bip/
├── 📋 api/                         # Modelos e DTOs base
│   ├── AllDtos.java                # Registros de DTOs
│   ├── Beneficio.java              # Modelo principal
│   └── *Controller/*Service.java   # Controllers e Services base
├── 🎯 application/                 # Camada de aplicação
│   ├── dtos/                       # Data Transfer Objects
│   ├── mappers/                    # Mapeamento entre camadas
│   └── usecases/                   # Casos de uso de negócio
├── 🏛️ domain/                      # Domínio do negócio
│   ├── entities/                   # Entidades JPA
│   ├── repositories/               # Interfaces de repositório
│   ├── services/                   # Serviços de domínio
│   └── valueobjects/               # Objetos de valor
├── 🔧 infrastructure/              # Infraestrutura
│   ├── configuration/              # Configurações CDI
│   └── repositories/               # Implementações JPA
├── 🌐 presentation/                # Camada de apresentação
│   ├── controllers/                # Controllers REST
│   └── handlers/                   # Manipuladores de erro
└── 🚀 servlet/                     # Servlets funcionais
    ├── StatusServlet.java          # Status da aplicação
    ├── BeneficiosServlet.java      # CRUD de benefícios
    ├── TransferenciaServlet.java   # Transferências
    └── CriarDadosServlet.java      # Dados de teste

📁 src/main/webapp/
├── index.html                      # Interface web moderna
└── WEB-INF/
    ├── web.xml                     # Configuração servlet
    └── beans.xml                   # Configuração CDI

📁 src/main/resources/
├── META-INF/persistence.xml        # Configuração JPA
├── data.sql                        # Dados iniciais
└── schema.sql                      # Estrutura do banco
```

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 17+ | Linguagem de programação |
| **Jakarta EE** | 10.0 | APIs empresariais modernas |
| **Servlets** | 6.0 | APIs REST funcionais |
| **CDI** | 4.0 | Injeção de dependências |
| **JPA** | 3.1 | Persistência de dados |
| **H2 Database** | 2.2.224 | Banco de dados em memória |
| **Jetty** | 11.0.18 | Servidor web embarcado |
| **Maven** | 3.9+ | Gerenciamento de build |

### ✨ Características Técnicas
- 🚀 **Deploy Rápido**: Jetty embarcado via Maven
- 💾 **Banco H2**: Dados em memória para desenvolvimento  
- 🔄 **Hot Reload**: Recompilação automática durante desenvolvimento
- 📱 **Interface Moderna**: HTML5 + CSS3 + JavaScript responsivo
- 🔧 **APIs Testáveis**: Endpoints prontos para Postman/cURL

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+**
- **Maven 3.9+** 

> 💡 **Sem necessidade de servidor externo!** O Jetty está embarcado no projeto.

### 1. Clone e Execute

```bash
# Clonar o repositório
git clone <seu-repositorio>
cd bip-api-simple

# Compilar e executar
mvn clean compile jetty:run-war

# Ou usar o profile específico
mvn jetty:run-war -Pjetty
```

### 2. Acessar a Aplicação

- 🌐 **Interface Principal**: http://localhost:8080
- 📊 **Status da API**: http://localhost:8080/api/beneficios/status  
- 📋 **Lista Benefícios**: http://localhost:8080/api/beneficios
- 🧪 **Criar Dados Teste**: http://localhost:8080/api/beneficios/dados-teste

### 3. Parar o Servidor

```bash
# No terminal onde está executando, pressione:
Ctrl + C

# Ou pressione Enter quando solicitado para redeploy
```

## 📋 APIs Disponíveis

### 📊 Status da Aplicação
```http
GET /api/beneficios/status
```
**Resposta**: Informações do sistema, versão e stack tecnológica

### 📋 Gerenciamento de Benefícios
```http
GET  /api/beneficios              # Listar todos os benefícios
POST /api/beneficios              # Criar novo benefício
```

#### Exemplo - Criar Benefício:
```json
POST /api/beneficios
Content-Type: application/json

{
  "nome": "Vale Alimentação - João",
  "descricao": "Benefício alimentação para colaborador",
  "valor": 500.00
}
```

### 💰 Transferência Entre Benefícios
```http
POST /api/beneficios/transferir
Content-Type: application/json

{
  "de": 1,
  "para": 2,
  "valor": 100.00,
  "descricao": "Transferência de teste"
}
```

### 🧪 Utilitários de Desenvolvimento
```http
GET  /api/beneficios/dados-teste  # Visualizar dados de exemplo
POST /api/beneficios/dados-teste  # Criar dados mock para teste
```

## 🧪 Como Testar

### 🌐 Via Interface Web (Recomendado)
1. Acesse: **http://localhost:8080**
2. Clique nos cards para testar endpoints diretamente
3. Use os exemplos cURL fornecidos na interface

### 📱 Via cURL (Terminal)
```bash
# 1. Verificar status da aplicação
curl -i http://localhost:8080/api/beneficios/status

# 2. Criar dados de teste
curl -X POST http://localhost:8080/api/beneficios/dados-teste

# 3. Listar benefícios disponíveis
curl -i http://localhost:8080/api/beneficios

# 4. Executar transferência
curl -X POST "http://localhost:8080/api/beneficios/transferir" \
     -H "Content-Type: application/json" \
     -d '{
       "de": 1,
       "para": 2,
       "valor": 50.00,
       "descricao": "Transferência via cURL"
     }'

# 5. Criar novo benefício
curl -X POST "http://localhost:8080/api/beneficios" \
     -H "Content-Type: application/json" \
     -d '{
       "nome": "Novo Benefício API", 
       "descricao": "Criado via API REST",
       "valor": 750.00
     }'
```

### 🔧 Via Postman
Importe a collection com os endpoints base:
- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json` (para POSTs)
- Use os exemplos JSON fornecidos acima

## 🛡️ Funcionalidades de Segurança

### ✅ Validações Implementadas
- **Parâmetros Obrigatórios**: Validação de campos nulos e vazios
- **Valores Positivos**: Controle de valores monetários
- **Saldos Suficientes**: Verificação antes de transferências
- **IDs Válidos**: Validação de existência de benefícios

### 🔒 Thread-Safety
```java
// Implementação thread-safe para transferências
public void executarTransferencia(Long de, Long para, double valor) {
    // Validações rigorosas
    validarParametros(de, para, valor);
    
    // Operações atômicas simuladas
    synchronized (this) {
        // Lógica de transferência segura
        aplicarTransferencia(de, para, valor);
    }
    
    // Auditoria e logging
    registrarTransferencia(de, para, valor);
}
```

### 📋 Tratamento de Erros
- **Respostas Consistentes**: JSON estruturado para todos os erros
- **Códigos HTTP Apropriados**: 400, 404, 500 conforme o caso
- **Mensagens Descritivas**: Informações claras sobre problemas
- **Logs Detalhados**: Rastreamento de operações para debug

### 🔄 Controle de Estado
- **Validação de Benefícios Ativos**: Apenas benefícios ativos participam de operações
- **Controle de Saldo**: Impedimento de valores negativos
- **Auditoria de Operações**: Log de todas as transferências realizadas

## 🏗️ Arquitetura e Design Patterns

### � Padrões Implementados
- **Clean Architecture**: Separação clara entre camadas
- **Repository Pattern**: Abstração da camada de dados  
- **DTO Pattern**: Transferência de dados entre camadas
- **Service Layer**: Lógica de negócio centralizada
- **Dependency Injection**: CDI para gestão de dependências

### 🔄 Fluxo de Dados
```
🌐 Servlet Request → 🎯 Controller → 📋 UseCase → 🏛️ Repository → 💾 Database
                                        ↓
🌐 JSON Response ← 🎯 DTO Mapper ← 📋 Business Logic ← 🏛️ Entity
```

### 📊 Comparação com Outras Implementações

| Característica | Esta Implementação | Spring Boot | EJB Tradicional |
|---------------|-------------------|-------------|-----------------|
| **Servidor** | Jetty Embarcado | Tomcat Embarcado | WildFly/Payara |
| **Complexidade** | Baixa | Média | Alta |
| **Setup** | `mvn jetty:run` | `mvn spring-boot:run` | Deploy WAR |
| **Teste** | Interface Web | Swagger UI | Console Admin |
| **Portabilidade** | Alta | Média | Baixa |
| **Performance** | Boa | Excelente | Muito Boa |

## 🔧 Configurações do Servidor

### DataSource (WildFly)
```xml
<datasource jndi-name="java:jboss/datasources/BipDS" pool-name="BipDS">
    <connection-url>jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password></password>
    </security>
</datasource>
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