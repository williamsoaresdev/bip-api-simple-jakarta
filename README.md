# 🏦 BIP API - Sistema de Gestão de Benefícios

<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.java.net/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-0052CC?logo=eclipse&logoColor=white)](https://jakarta.ee/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![H2](https://img.shields.io/badge/H2-Database-003e6b?logo=h2&logoColor=white)](https://www.h2database.com/)
[![Jetty](https://img.shields.io/badge/Jetty-11-FF6C2C?logo=eclipse-jetty&logoColor=white)](https://www.eclipse.org/jetty/)

[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen?logo=junit5&logoColor=white)](src/test/)
[![API](https://img.shields.io/badge/API-REST-blue?logo=swagger&logoColor=white)](http://localhost:8080/api)
[![Architecture](https://img.shields.io/badge/Architecture-Clean-success?logo=architecture&logoColor=white)](#-arquitetura)

</div>

## 📋 Sobre o Projeto

**BIP API** é um sistema moderno e robusto para gerenciamento de benefícios corporativos, desenvolvido seguindo os princípios da **Clean Architecture** e utilizando as mais recentes tecnologias **Jakarta EE 10**.

### 🎯 Funcionalidades Principais

- 💰 **Gestão Completa de Benefícios** - CRUD completo com validações robustas
- 🔄 **Sistema de Transferências** - Transferências entre benefícios com cálculo de taxas
- 📊 **Estatísticas e Relatórios** - Insights sobre utilização dos benefícios
- 🛡️ **Validação Avançada** - Bean Validation com regras de negócio consistentes
- 🌐 **APIs REST Completas** - Endpoints bem documentados e testados
- 🧪 **Cobertura de Testes** - Testes abrangentes para todas as funcionalidades

## 🛠️ Stack Tecnológica

### 🎯 Core Technologies
| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| **Java** | 17+ | Linguagem principal (LTS) |
| **Jakarta EE** | 10 | Plataforma empresarial |
| **Maven** | 3.9+ | Gerenciamento de dependências |
| **H2 Database** | 2.2+ | Banco em memória (dev/test) |
| **Jetty** | 11.0.18 | Servidor de aplicação |

### 🔧 Jakarta EE Stack
| Especificação | Implementação | Versão | Uso |
|---------------|---------------|--------|-----|
| **CDI** | Weld | 5.1.2 | Injeção de dependências |
| **JAX-RS** | Jersey | 3.1.5 | APIs REST |
| **JPA** | Hibernate | 6.4.4 | Persistência ORM |
| **Bean Validation** | Hibernate Validator | 8.0.1 | Validações |
| **JSON-B** | Eclipse Yasson | 3.0.3 | Serialização JSON |

### 🧪 Ferramentas de Qualidade
| Ferramenta | Versão | Finalidade |
|------------|--------|------------|
| **JUnit** | 5.10.1 | Framework de testes |
| **Mockito** | 5.8.0 | Mocking e simulação |
| **AssertJ** | 3.24.2 | Assertions fluentes |
| **JaCoCo** | 0.8.11 | Cobertura de código |
| **Checkstyle** | 10.12.6 | Qualidade de código |
| **PMD** | 6.55.0 | Análise estática |
| **SpotBugs** | 4.8.1 | Detecção de bugs |

## 🏗️ Arquitetura Clean Architecture

O projeto implementa **Clean Architecture** com separação clara de responsabilidades:

```
� com.bip
├── 🔵 domain/                 # 🏛️ CAMADA DE DOMÍNIO
│   ├── entities/             # Entidades de negócio
│   │   └── Beneficio.java    # Entidade principal
│   ├── valueobjects/         # Objetos de valor
│   │   ├── Dinheiro.java     # Representação monetária
│   │   └── Taxa.java         # Cálculo de taxas
│   ├── repositories/         # Contratos de persistência
│   │   └── BeneficioRepository.java
│   └── services/             # Serviços de domínio
│       └── BeneficioService.java
│
├── 🟢 application/           # 💼 CAMADA DE APLICAÇÃO
│   ├── dtos/                 # Data Transfer Objects
│   │   ├── BeneficioDto.java
│   │   ├── CriarBeneficioDto.java
│   │   ├── AtualizarBeneficioDto.java
│   │   └── TransferenciaDto.java
│   ├── mappers/              # Mapeamento entidade ↔ DTO
│   │   └── BeneficioMapper.java
│   ├── services/             # Serviços de aplicação
│   │   └── BeneficioApplicationService.java
│   └── usecases/             # Casos de uso
│       ├── BeneficioUseCase.java
│       └── TransferenciaUseCase.java
│
├── 🟡 infrastructure/        # 🔧 CAMADA DE INFRAESTRUTURA
│   ├── configuration/        # Configurações CDI/JAX-RS
│   │   ├── EntityManagerProducer.java
│   │   └── JaxRsConfiguration.java
│   └── persistence/          # Implementações JPA
│       └── BeneficioRepositoryImpl.java
│
└── 🔴 presentation/          # 🌐 CAMADA DE APRESENTAÇÃO
    ├── controllers/          # Controllers REST
    │   ├── BeneficioController.java
    │   └── TransferenciaController.java
    ├── handlers/             # Exception Handlers
    │   └── GlobalExceptionHandler.java
    └── utils/                # Utilitários de apresentação
        └── ErrorResponseBuilder.java
```

### 📐 Princípios Arquiteturais

- **🎯 Dependency Inversion**: Dependências apontam para abstrações
- **🔒 Single Responsibility**: Cada classe tem uma única responsabilidade
- **🔗 Dependency Injection**: CDI gerencia todas as dependências
- **🧪 Testability**: Todas as camadas são facilmente testáveis
- **🔄 Separation of Concerns**: Separação clara entre camadas

## 🚀 Quick Start

### 📋 Pré-requisitos

- ☕ **Java 17+** (OpenJDK ou Oracle)
- 📦 **Maven 3.9+**
- 🌐 **Git** (para clonar o repositório)

### 1️⃣ Clonar e Configurar

```bash
# Clonar repositório
git clone https://github.com/williamsoaresdev/bip-api-simple-jakarta.git
cd bip-api-simple-jakarta

# Verificar pré-requisitos
java --version    # Deve mostrar Java 17+
mvn --version     # Deve mostrar Maven 3.9+
```

### 2️⃣ Compilar e Testar

```bash
# Compilar projeto
mvn clean compile

# Executar testes (opcional)
mvn test

# Gerar relatórios de qualidade (opcional)
mvn clean verify
```

### 3️⃣ Iniciar Aplicação

```bash
# Iniciar servidor Jetty
mvn jetty:run

# Aguardar mensagem: "Started Server@..."
# Aplicação estará disponível em: http://localhost:8080
```

### 4️⃣ Verificar Funcionamento

```bash
# Testar API status
curl http://localhost:8080/api/beneficios/status

# Ou abrir no navegador:
# http://localhost:8080/api/beneficios/status
```

### 5️⃣ Parar Aplicação

```bash
# No terminal onde está executando:
Ctrl + C
```

## 📡 Documentação da API

### 🏠 Base URL
```
http://localhost:8080/api
```

### 📊 Status e Saúde

#### Status da Aplicação
```http
GET /beneficios/status
```
**Resposta**: Informações da aplicação, versão e endpoints disponíveis

<details>
<summary>📄 Exemplo de Resposta</summary>

```json
{
  "status": "API funcionando",
  "timestamp": "2025-10-24T12:30:00",
  "message": "Clean Architecture implementada com sucesso",
  "version": "3.0.0-Clean-Architecture",
  "endpoints": [
    "GET /api/beneficios - Lista todos os benefícios",
    "POST /api/beneficios - Cria novo benefício",
    "GET /api/beneficios/{id} - Busca benefício por ID",
    "PUT /api/beneficios/{id} - Atualiza benefício",
    "DELETE /api/beneficios/{id} - Remove benefício",
    "GET /api/beneficios/ativos - Lista benefícios ativos",
    "GET /api/beneficios/estatisticas - Estatísticas dos benefícios"
  ]
}
```
</details>

---

### � Gerenciamento de Benefícios

#### Listar Todos os Benefícios
```http
GET /beneficios
```

#### Listar Apenas Benefícios Ativos
```http
GET /beneficios/ativos
```

#### Buscar Benefício por ID
```http
GET /beneficios/{id}
```

#### Criar Novo Benefício
```http
POST /beneficios
Content-Type: application/json

{
  "nome": "Vale Alimentação",
  "descricao": "Benefício para compra de alimentos",
  "valorInicial": 500.00
}
```

<details>
<summary>📋 Validações</summary>

- **nome**: 3-100 caracteres (obrigatório)
- **descricao**: até 500 caracteres
- **valorInicial**: ≥ 0.00 (obrigatório)
</details>

#### Atualizar Benefício
```http
PUT /beneficios/{id}
Content-Type: application/json

{
  "nome": "Vale Alimentação Premium",
  "descricao": "Benefício premium para alimentação",
  "valorInicial": 750.00
}
```

#### Remover Benefício
```http
DELETE /beneficios/{id}
```

#### Estatísticas dos Benefícios
```http
GET /beneficios/estatisticas
```

<details>
<summary>📄 Exemplo de Resposta</summary>

```json
{
  "totalBeneficiosAtivos": 5,
  "somaTotalValores": 2500.00,
  "timestamp": "2025-10-24T12:30:00"
}
```
</details>

---

### � Sistema de Transferências

#### Executar Transferência
```http
POST /transferencias
Content-Type: application/json

{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00,
  "descricao": "Transferência entre benefícios"
}
```

<details>
<summary>📋 Regras de Negócio</summary>

- Benefícios origem e destino devem ser diferentes
- Saldo suficiente no benefício origem
- Ambos benefícios devem estar ativos
- Valor deve ser > 0.01
- Taxa de 2% aplicada automaticamente
</details>

#### Validar Transferência (Preview)
```http
POST /transferencias/validar
Content-Type: application/json

{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00
}
```

#### Calcular Taxa de Transferência
```http
GET /transferencias/taxa?valor=100.00
```

<details>
<summary>📄 Exemplo de Resposta</summary>

```json
{
  "valorOriginal": 100.00,
  "taxa": 2.00,
  "valorComTaxa": 102.00
}
```
</details>

## 🧪 Testando a API

### 🔗 Coleções Postman

O projeto inclui coleções completas do Postman:

- **📁 BIP-API-Collection.postman_collection.json** - Coleção principal
- **🌍 BIP-API-Development.postman_environment.json** - Environment de desenvolvimento
- **📖 POSTMAN-COLLECTIONS.md** - Documentação completa

#### Importar no Postman:
1. Abra o Postman
2. Import → File → Selecione os arquivos `.json`
3. Selecione o environment "BIP API - Development"
4. Execute os requests!

### 💻 Scripts de Teste

#### Para Linux/macOS:
```bash
./scripts/test-api.sh
```

#### Para Windows (PowerShell):
```powershell
.\scripts\test-api.ps1
```

#### Para Windows (Batch):
```cmd
scripts\test-api.bat
```

### 🚀 Exemplos com cURL

<details>
<summary>📝 Exemplos Básicos</summary>

```bash
# Verificar status
curl http://localhost:8080/api/beneficios/status

# Listar benefícios
curl http://localhost:8080/api/beneficios

# Criar benefício
curl -X POST "http://localhost:8080/api/beneficios" \
     -H "Content-Type: application/json" \
     -d '{
       "nome": "Vale Transporte",
       "descricao": "Benefício para transporte público",
       "valorInicial": 300.00
     }'

# Executar transferência
curl -X POST "http://localhost:8080/api/transferencias" \
     -H "Content-Type: application/json" \
     -d '{
       "beneficioOrigemId": 1,
       "beneficioDestinoId": 2,
       "valor": 50.00,
       "descricao": "Transferência de teste"
     }'
```
</details>

## 🧪 Testes e Qualidade

### 🎯 Cobertura de Testes

```bash
# Executar todos os testes
mvn test

# Gerar relatório de cobertura
mvn clean test jacoco:report

# Visualizar relatório
open target/site/jacoco/index.html
```

### 📊 Relatórios de Qualidade

```bash
# Análise completa de qualidade
mvn clean verify

# Relatórios disponíveis em:
# - target/site/jacoco/index.html (Cobertura)
# - target/site/checkstyle.html (Estilo)
# - target/site/pmd.html (PMD)
# - target/site/spotbugs.html (SpotBugs)
```

### 🏆 Métricas Atuais

| Métrica | Valor | Status |
|---------|-------|--------|
| **Testes** | Todos passando | ✅ |
| **Cobertura** | Alta cobertura | ✅ |
| **Checkstyle** | 0 violações críticas | ✅ |
| **PMD** | Violações menores | ⚠️ |
| **SpotBugs** | Sem bugs detectados | ✅ |
| **Build** | Passing | ✅ |

## 🔧 Configuração e Customização

### 🌍 Profiles do Maven

```bash
# Desenvolvimento (padrão)
mvn jetty:run

# Produção
mvn clean package -Pprod

# Análise de qualidade
mvn clean verify -Pquality
```

### 🗄️ Configuração de Banco

#### H2 (Desenvolvimento - Padrão)
```properties
# Configuração automática
# URL: jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
# User: sa
# Password: (vazio)
# Console: http://localhost:8080/h2-console (se habilitado)
```

#### PostgreSQL (Produção)
```properties
# src/main/resources/persistence.xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/bipdb"/>
<property name="jakarta.persistence.jdbc.user" value="bip_user"/>
<property name="jakarta.persistence.jdbc.password" value="sua_senha"/>
```

### ⚙️ Configurações de Performance

```xml
<!-- pom.xml - Jetty Configuration -->
<plugin>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <configuration>
        <httpConnector>
            <port>8080</port>
            <idleTimeout>60000</idleTimeout>
        </httpConnector>
        <webApp>
            <contextPath>/</contextPath>
        </webApp>
    </configuration>
</plugin>
```

## 🚦 Troubleshooting

### ❗ Problemas Comuns

<details>
<summary>🔧 Porta 8080 já em uso</summary>

```bash
# Verificar quem está usando a porta
netstat -tulpn | grep :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Usar porta diferente
mvn jetty:run -Djetty.http.port=8081
```
</details>

<details>
<summary>☕ Versão do Java incorreta</summary>

```bash
# Verificar versão
java --version

# Deve mostrar Java 17 ou superior
# Configurar JAVA_HOME se necessário

# Linux/Mac
export JAVA_HOME=/path/to/java17

# Windows
set JAVA_HOME=C:\path\to\java17
```
</details>

<details>
<summary>🧪 Testes falhando</summary>

```bash
# Limpar e recompilar
mvn clean compile test

# Executar teste específico
mvn test -Dtest=BeneficioControllerTest

# Debug de teste
mvn test -Dmaven.surefire.debug
```
</details>

<details>
<summary>💾 Problemas de banco de dados</summary>

```bash
# Limpar dados H2 (reiniciar com banco limpo)
mvn clean

# Verificar logs de SQL
mvn jetty:run -Dhibernate.show_sql=true
```
</details>

## 📚 Recursos Adicionais

### 📖 Documentação

- **[📁 Coleções Postman](POSTMAN-COLLECTIONS.md)** - Guia completo das coleções
- **[🧪 Scripts de Teste](scripts/test-api.sh)** - Testes automatizados via cURL
- **[🔧 Scripts PowerShell](scripts/test-api.ps1)** - Testes para Windows  
- **[📊 Scripts Batch](scripts/test-api.bat)** - Testes básicos Windows
- **[� Scripts de Qualidade](scripts/quality-check.sh)** - Análise de qualidade completa
- **[�📐 Documentação Arquitetura](docs/ARCHITECTURE.md)** - Clean Architecture
- **[📖 API Reference](docs/API-REFERENCE.md)** - Documentação completa da API
- **[🚀 Guia de Deployment](docs/DEPLOYMENT.md)** - Docker, K8s, Cloud
- **[🔧 Postman Collections](postman/)** - Testes e validação automática
- **[⚙️ Configurações de Qualidade](config/quality/)** - CheckStyle, PMD, SpotBugs### 🔗 Links Úteis

- **[Jakarta EE 10](https://jakarta.ee/)** - Documentação oficial
- **[Hibernate ORM](https://hibernate.org/orm/)** - Guia do Hibernate
- **[Eclipse Jetty](https://www.eclipse.org/jetty/)** - Documentação do Jetty
- **[Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)** - Artigo original

### 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. Commit suas mudanças (`git commit -m 'Add amazing feature'`)
4. Push para a branch (`git push origin feature/amazing-feature`)
5. Abra um Pull Request

### 📞 Suporte

- 🐛 **Issues**: [GitHub Issues](https://github.com/williamsoaresdev/bip-api-simple-jakarta/issues)
- 📧 **Email**: suporte@bip-api.com
- 💬 **Discussões**: [GitHub Discussions](https://github.com/williamsoaresdev/bip-api-simple-jakarta/discussions)

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">

**🏗️ Desenvolvido com Clean Architecture + Jakarta EE 10**

[![Made with ❤️](https://img.shields.io/badge/Made%20with-❤️-red.svg)](https://github.com/williamsoaresdev)
[![Powered by Jakarta EE](https://img.shields.io/badge/Powered%20by-Jakarta%20EE-blue.svg)](https://jakarta.ee/)

</div>