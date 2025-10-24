# 🔧 Configurações de Qualidade de Código

Esta pasta contém todas as configurações para ferramentas de análise de qualidade de código utilizadas no projeto BIP API.

## 📁 Arquivos de Configuração

### 1. `checkstyle.xml`
**Ferramenta:** CheckStyle  
**Propósito:** Verificação de estilo de código e convenções  

**Principais Verificações:**
- ✅ Comprimento de linha (máx. 120 caracteres)
- ✅ Convenções de nomenclatura (classes, métodos, variáveis)
- ✅ Uso adequado de imports
- ✅ Estrutura de blocos e indentação
- ✅ Espaçamento e formatação

**Execução:**
```bash
mvn checkstyle:check
```

### 2. `pmd-ruleset.xml`
**Ferramenta:** PMD (Programming Mistake Detector)  
**Propósito:** Detecção de problemas de código e boas práticas  

**Principais Categorias:**
- 🎯 **Best Practices**: Melhores práticas de programação
- 🎨 **Code Style**: Estilo e legibilidade do código
- 🏗️ **Design**: Princípios de design e arquitetura
- ❌ **Error Prone**: Detecção de erros comuns
- ⚡ **Performance**: Otimizações de performance
- 🔒 **Security**: Verificações de segurança

**Execução:**
```bash
mvn pmd:check
mvn pmd:cpd-check  # Detecção de código duplicado
```

### 3. `spotbugs-exclude.xml`
**Ferramenta:** SpotBugs  
**Propósito:** Detecção de bugs e vulnerabilidades  

**Exclusões Configuradas:**
- 📦 Configurações de infraestrutura
- 🧪 Classes de teste
- 📋 DTOs (serialização)
- 🏛️ Entidades JPA (campos não utilizados)
- 🔍 Métodos de validação

**Execução:**
```bash
mvn spotbugs:check
```

## 🚀 Execução Completa

### Script Automatizado
```bash
# Linux/macOS
./scripts/quality-check.sh

# Windows
scripts\quality-check.bat
```

### Maven Profiles
```bash
# Profile de qualidade completo
mvn clean verify -Pquality

# Relatórios individuais
mvn checkstyle:check
mvn pmd:check
mvn spotbugs:check
mvn jacoco:report
```

## 📊 Relatórios Gerados

Após execução, os relatórios ficam disponíveis em:

| Ferramenta | Localização |
|------------|-------------|
| **CheckStyle** | `target/site/checkstyle.html` |
| **PMD** | `target/site/pmd.html` |
| **SpotBugs** | `target/site/spotbugs.html` |
| **JaCoCo** | `target/site/jacoco/index.html` |
| **Site Completo** | `target/site/index.html` |

## ⚙️ Configurações Personalizadas

### CheckStyle
- **Line Length**: 120 caracteres
- **Naming**: Convenções Java padrão
- **Imports**: Sem imports com wildcard (*)
- **Whitespace**: Configuração customizada

### PMD
- **Complexity**: Limite de complexidade ciclomática
- **Method Length**: Máximo 50 linhas por método
- **Parameter Count**: Máximo 7 parâmetros
- **Custom Rules**: Regras específicas para Jakarta EE

### SpotBugs
- **Security**: Verificações de segurança habilitadas
- **Performance**: Detecção de problemas de performance
- **Nullness**: Análise de null pointer exceptions
- **Jakarta EE**: Exclusões específicas para CDI/JPA

## 🔧 Customização

### Adicionando Novas Regras

#### CheckStyle
```xml
<module name="TreeWalker">
    <module name="NovaRegra">
        <property name="parametro" value="valor"/>
    </module>
</module>
```

#### PMD
```xml
<rule ref="category/java/bestpractices.xml/NovaRegra">
    <properties>
        <property name="parametro" value="valor"/>
    </properties>
</rule>
```

#### SpotBugs (Exclusões)
```xml
<Match>
    <Package name="com.bip.novo.pacote"/>
    <Bug pattern="PATTERN_NAME"/>
</Match>
```

## 📈 Métricas de Qualidade

### Metas do Projeto
- **Cobertura de Testes**: ≥ 95%
- **Violations PMD**: < 10
- **CheckStyle Issues**: 0
- **SpotBugs**: 0 bugs críticos
- **Duplicação**: < 3%

### Thresholds Configurados
```xml
<!-- JaCoCo -->
<limit>
    <counter>BRANCH</counter>
    <value>COVEREDRATIO</value>
    <minimum>0.95</minimum>
</limit>
```

## 🛡️ CI/CD Integration

### GitHub Actions
```yaml
- name: Quality Check
  run: |
    mvn clean verify -Pquality
    mvn jacoco:check
```

### Falha no Build
O build falhará se:
- Cobertura < 95%
- Bugs críticos detectados
- Violações de segurança

## 📚 Documentação Adicional

- **[CheckStyle Rules](https://checkstyle.org/checks.html)**
- **[PMD Rules](https://pmd.github.io/latest/pmd_rules_java.html)**
- **[SpotBugs Bug Patterns](https://spotbugs.readthedocs.io/en/latest/bugDescriptions.html)**
- **[JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)**

## 🔄 Manutenção

### Atualizações Regulares
1. Revisar regras trimestralmente
2. Atualizar thresholds baseado em métricas
3. Adicionar novas regras conforme necessário
4. Sincronizar com padrões da equipe