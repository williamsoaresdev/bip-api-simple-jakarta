# Arquitetura do Sistema BIP API

## Visão Geral

O projeto segue os princípios da **Clean Architecture** (Arquitetura Limpa), organizando o código em camadas bem definidas que garantem separação de responsabilidades e baixo acoplamento.

## Estrutura de Camadas

### 1. Domain (Domínio)
Camada central que contém a lógica de negócio pura.

#### Entities (Entidades)
- **Beneficio**: Entidade principal que representa um benefício do sistema
  - Possui propriedades: id, nome, descrição, valor, ativo, timestamps
  - Implementa regras de negócio fundamentais
  - Validações de domínio

#### Value Objects
- **Saldo**: Representa valores monetários com validações específicas
- **Status**: Enumerações para estados do sistema

#### Repositories (Interfaces)
- **BeneficioRepository**: Interface que define operações de persistência
- Contratos para acesso a dados sem implementação

#### Services (Serviços de Domínio)
- **BeneficioService**: Lógica de negócio complexa
- **TransferenciaService**: Regras para transferências entre benefícios

### 2. Application (Aplicação)
Camada de casos de uso e orquestração.

#### Use Cases
- **BeneficioUseCase**: Orquestra operações de benefícios
- **TransferenciaUseCase**: Coordena transferências e validações

#### DTOs (Data Transfer Objects)
- **BeneficioDTO**: Transferência de dados de benefícios
- **TransferenciaDTO**: Dados para operações de transferência
- **ErrorResponseDTO**: Padronização de respostas de erro

#### Mappers
- **BeneficioMapper**: Conversão entre entidades e DTOs
- Isolamento entre camadas

#### Services
- **BeneficioApplicationService**: Coordenação de casos de uso
- **TransferenciaApplicationService**: Lógica de aplicação para transferências

### 3. Infrastructure (Infraestrutura)
Implementações técnicas e acesso a recursos externos.

#### Configuration
- **EntityManagerProducer**: Configuração JPA/Hibernate
- **CorsFilter**: Configuração CORS
- **ExceptionMapperConfig**: Mapeamento de exceções

#### Persistence
- **BeneficioJpaRepository**: Implementação JPA do repositório
- Queries e operações de banco de dados

### 4. Presentation (Apresentação)
Interface com o mundo externo.

#### Controllers
- **BeneficioController**: Endpoints REST para benefícios
- **TransferenciaController**: Endpoints para transferências

#### Utils
- **ErrorResponseBuilder**: Construção padronizada de respostas de erro

#### Handlers
- **GlobalExceptionHandler**: Tratamento global de exceções

## Fluxo de Dados

```
Request → Controller → UseCase → Service → Repository → Database
                    ↓
Response ← DTO ← Mapper ← Entity ← Repository ← Database
```

## Tecnologias Utilizadas

### Core Framework
- **Jakarta EE 10**: Plataforma enterprise
- **CDI (Weld)**: Injeção de dependência
- **JAX-RS (Jersey)**: REST APIs

### Persistência
- **JPA/Hibernate**: ORM
- **H2 Database**: Base de dados em memória

### Build & Deploy
- **Maven**: Gerenciamento de dependências
- **Jetty**: Servidor de aplicação

## Padrões Implementados

### Design Patterns
- **Repository Pattern**: Abstração de acesso a dados
- **DTO Pattern**: Transferência de dados entre camadas
- **Mapper Pattern**: Conversão entre objetos
- **Builder Pattern**: Construção de objetos complexos

### Princípios SOLID
- **Single Responsibility**: Cada classe tem uma responsabilidade
- **Open/Closed**: Aberto para extensão, fechado para modificação
- **Liskov Substitution**: Substituição de implementações
- **Interface Segregation**: Interfaces específicas
- **Dependency Inversion**: Dependência de abstrações

## Configuração de Ambiente

### Persistence Unit
```xml
<persistence-unit name="bipPU">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <class>com.bip.domain.entities.Beneficio</class>
    <!-- Configurações específicas -->
</persistence-unit>
```

### CDI Configuration
```xml
<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       bean-discovery-mode="annotated">
</beans>
```

## Vantagens da Arquitetura

### Testabilidade
- Camadas isoladas facilitam testes unitários
- Mocks e stubs para dependências externas

### Manutenibilidade
- Código organizado e bem estruturado
- Facilita mudanças e evoluções

### Escalabilidade
- Separação clara de responsabilidades
- Facilita adição de novas funcionalidades

### Flexibilidade
- Baixo acoplamento entre camadas
- Fácil substituição de implementações

## Próximos Passos

### Melhorias Sugeridas
1. **Implementar Cache**: Redis ou Hazelcast
2. **Adicionar Métricas**: Micrometer/Prometheus
3. **Implementar Auditoria**: Logs estruturados
4. **Adicionar Segurança**: JWT/OAuth2
5. **Testes de Integração**: TestContainers