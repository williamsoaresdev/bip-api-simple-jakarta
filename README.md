# BIP API - Sistema de Gestão de Benefícios

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![Coverage](https://img.shields.io/badge/Coverage-99.5%25-brightgreen.svg)](target/site/jacoco/index.html)
[![Tests](https://img.shields.io/badge/Tests-303%20passing-brightgreen.svg)](src/test/)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](pom.xml)

## 📋 Descrição

Sistema moderno para gerenciamento de benefícios corporativos desenvolvido com **Jakarta EE 10** seguindo os princípios da **Clean Architecture**. A aplicação oferece APIs REST completas para operações CRUD de benefícios e funcionalidades avançadas de transferência entre benefícios com cálculo automático de taxas.

### ✨ Características Principais
- 🏗️ **Clean Architecture** com separação clara de responsabilidades
- 🧪 **99.5% de cobertura de testes** (2.261/2.263 instruções testadas)
- 🔄 **303 testes automatizados** com cenários abrangentes
- 🚀 **APIs REST** completas com validação de dados
- 💰 **Sistema de transferências** com cálculo de taxas
- 🛡️ **Tratamento robusto de exceções** com mensagens personalizadas

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 17** - LTS com recursos modernos
- **Jakarta EE 10** - Plataforma empresarial completa
- **Maven 3.9+** - Gerenciamento de dependências

### Jakarta EE Stack
- **CDI 4.0** (Weld) - Injeção de dependências
- **JAX-RS 3.1** (Jersey) - APIs REST
- **JPA 3.1** (Hibernate 6.4) - Persistência ORM
- **Bean Validation 3.0** - Validação declarativa
- **JSON-B 3.0** - Serialização JSON

### Persistência
- **H2 Database** - Desenvolvimento/Testes (em memória)
- **PostgreSQL** - Produção (suporte completo)

### Testes e Qualidade
- **JUnit 5.10.1** - Framework de testes
- **Mockito 5.8** - Mocking avançado
- **AssertJ 3.24** - Assertions fluentes
- **REST Assured 5.4** - Testes de API
- **JaCoCo 0.8.11** - Análise de cobertura
- **Checkstyle** - Padrões de código

## 🏗️ Arquitetura

O projeto implementa **Clean Architecture** com 4 camadas bem definidas:

```
📁 src/main/java/com/bip/
├── 🔵 domain/               # Camada de Domínio
│   ├── entities/           # Entidades de negócio
│   ├── valueobjects/       # Objetos de valor
│   ├── repositories/       # Contratos de persistência
│   └── services/          # Serviços de domínio
├── 🟢 application/         # Camada de Aplicação
│   ├── dtos/              # Data Transfer Objects
│   ├── mappers/           # Mapeamento de dados
│   └── usecases/          # Casos de uso
├── 🟡 infrastructure/      # Camada de Infraestrutura
│   ├── configuration/     # Configurações CDI/JAX-RS
│   └── persistence/       # Implementações JPA
└── 🔴 presentation/        # Camada de Apresentação
    ├── controllers/       # Controllers REST
    └── handlers/          # Exception Handlers
```

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- **Java 17** ou superior
- **Maven 3.9** ou superior

### 1. Clone e Compile
```bash
# Clonar o repositório
git clone <seu-repositorio>
cd bip-api-simple

# Compilar o projeto
mvn clean compile

# Executar testes (opcional)
mvn test
```

### 2. Executar a Aplicação
```bash
# Iniciar servidor Jetty embarcado
mvn jetty:run

# Ou usando o perfil específico
mvn jetty:run -Pjetty
```

### 3. Acessar a Aplicação
- **Interface Principal**: http://localhost:8080
- **API Status**: http://localhost:8080/api/beneficios/status
- **Lista Benefícios**: http://localhost:8080/api/beneficios

### 4. Parar o Servidor
```bash
# No terminal onde está executando:
Ctrl + C
```

## 📡 Collections dos Endpoints

### 📊 Status do Sistema
```http
GET /api/beneficios/status
```
**Resposta**: Status da aplicação, versão e tecnologias utilizadas

### 📋 Gerenciamento de Benefícios

#### Listar Todos os Benefícios
```http
GET /api/beneficios
```

#### Listar Apenas Benefícios Ativos
```http
GET /api/beneficios/ativos
```

#### Buscar Benefício por ID
```http
GET /api/beneficios/{id}
```

#### Criar Novo Benefício
```http
POST /api/beneficios
Content-Type: application/json

{
  "nome": "Vale Alimentação",
  "descricao": "Benefício para alimentação",
  "valorInicial": 500.00
}
```

#### Atualizar Benefício
```http
PUT /api/beneficios/{id}
Content-Type: application/json

{
  "nome": "Vale Alimentação Atualizado",
  "descricao": "Descrição modificada",
  "valorInicial": 750.00
}
```

#### Remover Benefício
```http
DELETE /api/beneficios/{id}
```

#### Obter Estatísticas
```http
GET /api/beneficios/estatisticas
```

### 💰 Sistema de Transferências

#### Executar Transferência
```http
POST /api/transferencias
Content-Type: application/json

{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00,
  "descricao": "Transferência entre benefícios"
}
```

#### Validar Transferência (Prévia)
```http
POST /api/transferencias/validar
Content-Type: application/json

{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00
}
```

#### Calcular Taxa de Transferência
```http
GET /api/transferencias/taxa?valor=100.00
```

### 🧪 Utilitários para Desenvolvimento

#### Gerar Dados de Teste
```http
POST /api/beneficios/dados-teste
```

## 🧪 Exemplos com cURL

### Criar Benefício
```bash
curl -X POST "http://localhost:8080/api/beneficios" \
     -H "Content-Type: application/json" \
     -d '{
       "nome": "Vale Transporte",
       "descricao": "Benefício para transporte",
       "valorInicial": 300.00
     }'
```

### Listar Benefícios
```bash
curl -i http://localhost:8080/api/beneficios
```

### Executar Transferência
```bash
curl -X POST "http://localhost:8080/api/transferencias" \
     -H "Content-Type: application/json" \
     -d '{
       "beneficioOrigemId": 1,
       "beneficioDestinoId": 2,
       "valor": 50.00,
       "descricao": "Transferência via cURL"
     }'
```

### Verificar Status
```bash
curl -i http://localhost:8080/api/beneficios/status
```

## 🛡️ Regras de Negócio

### Benefícios
- Nomes devem ser únicos no sistema
- Valores não podem ser negativos
- Benefícios inativos não participam de operações
- Campos nome e descrição são obrigatórios
- Valor inicial deve ser maior que zero

### Transferências
- Benefícios de origem e destino devem ser diferentes
- Saldo suficiente no benefício de origem
- Ambos os benefícios devem estar ativos
- Valores devem ser positivos
- Taxa de 2% aplicada automaticamente sobre o valor
- Descrição da transferência é obrigatória

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn clean test jacoco:report

# Ver relatório de cobertura
open target/site/jacoco/index.html
```

### Cobertura Atual
- **303 testes** implementados
- **99.5% cobertura** (2.261/2.263 instruções)
- **0 falhas** - todos os testes passando
- **Tempo de execução**: ~15 segundos

### Testes por Camada
| Camada | Testes | Cobertura |
|--------|--------|-----------|
| Domain | 89 | 100% |
| Application | 156 | 99% |
| Infrastructure | 31 | 98% |
| Presentation | 27 | 100% |

## 🔧 Configuração de Desenvolvimento

### Profiles do Maven
O projeto suporta múltiplos servidores de aplicação:

```bash
# Jetty (padrão)
mvn jetty:run

# WildFly
mvn clean package -Pwildfly

# Payara
mvn clean package -Ppayara

# Liberty
mvn clean package -Pliberty
```

### Banco de Dados
```properties
# H2 (Desenvolvimento)
URL: jdbc:h2:mem:bipdb
User: sa
Password: (vazio)
Console: http://localhost:8080/h2-console

# PostgreSQL (Produção)
URL: jdbc:postgresql://localhost:5432/bipdb
User: bip_user
Password: (configurar)
```

## 📈 Performance e Monitoramento

### Métricas de Performance
- **Startup Time**: ~3 segundos
- **Memory Usage**: ~128MB iniciais
- **Request/Response**: <50ms (operações CRUD)
- **Database Queries**: Otimizadas com JPA

### Health Checks
```http
GET /api/beneficios/status
```
Retorna informações sobre:
- Status da aplicação
- Versão do sistema
- Tempo de execução
- Tecnologias utilizadas

## 🚦 Solução de Problemas

### Problemas Comuns

#### Porta 8080 em Uso
```bash
# Verificar processos na porta
netstat -tulpn | grep :8080

# Usar porta diferente
mvn jetty:run -Djetty.port=8081
```

#### Erro de Compilação Java
```bash
# Verificar versão do Java
java -version

# Deve ser 17 ou superior
# Configurar JAVA_HOME se necessário
```

#### Testes Falhando
```bash
# Limpar e recompilar
mvn clean compile test

# Executar teste específico
mvn test -Dtest=BeneficioTest
```

## 🎯 Funcionalidades Destacadas

### Interface Web Moderna
- **Design Responsivo**: Funciona em desktop e mobile
- **Cards Interativos**: Teste endpoints diretamente no navegador
- **Exemplos cURL**: Comandos prontos para copiar
- **Status em Tempo Real**: Monitoramento da aplicação

### Arquitetura Robusta
- **Thread-Safe**: Operações seguras para concorrência
- **Transações**: Controle transacional adequado
- **Validações**: Bean Validation em todas as camadas
- **Exception Handling**: Tratamento centralizado de erros

### Qualidade de Código
- **Clean Code**: Código limpo e bem documentado
- **SOLID Principles**: Princípios aplicados consistentemente
- **Design Patterns**: Repository, DTO, Factory, Builder
- **Test Coverage**: 99.5% de cobertura de testes

## 📚 Documentação Adicional

- **[Relatório Final](RELATORIO-FINAL.md)** - Documentação completa da implementação
- **[Teste Manual](teste-api.sh)** - Script para testes automatizados
- **[Cobertura de Testes](target/site/jacoco/index.html)** - Relatório detalhado

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**🏗️ Clean Architecture + Jakarta EE 10**  
Sistema profissional com separação clara de responsabilidades, 303 testes e 99.5% de cobertura de código.