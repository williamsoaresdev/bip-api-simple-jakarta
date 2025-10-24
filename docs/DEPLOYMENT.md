# Guia de Deployment - BIP API

## Visão Geral

Este documento apresenta as diferentes opções de deployment para a aplicação BIP API, desde ambiente de desenvolvimento até produção.

## Pré-requisitos

### Requisitos de Sistema
- **Java**: 17 ou superior (LTS recomendado)
- **Maven**: 3.8+ para build
- **Memória**: Mínimo 512MB RAM, recomendado 1GB+
- **Disco**: Mínimo 100MB livre
- **Rede**: Porta 8080 disponível (configurável)

### Requisitos de Banco de Dados
- **Desenvolvimento**: H2 (incluído)
- **Produção**: PostgreSQL 12+, MySQL 8+, ou Oracle 12c+

---

## Deployment Local (Desenvolvimento)

### 1. Usando Maven + Jetty
```bash
# Clonar repositório
git clone <repository-url>
cd bip-api-simple

# Compilar e executar
mvn clean compile jetty:run

# Ou usar o script
./scripts/start-dev.sh  # Linux/Mac
scripts/start-dev.bat   # Windows
```

**Acesso:** `http://localhost:8080`

### 2. Usando JAR Executável
```bash
# Gerar JAR
mvn clean package

# Executar
java -jar target/bip-api-simple-1.0.0-SNAPSHOT.jar
```

---

## Deployment com Docker

### 1. Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

# Metadados
LABEL maintainer="Equipe BIP"
LABEL version="1.0.0"
LABEL description="BIP API - Sistema de Benefícios"

# Configurar usuário não-root
RUN groupadd -r bipuser && useradd -r -g bipuser bipuser

# Diretório de trabalho
WORKDIR /app

# Copiar JAR
COPY target/bip-api-simple-*.jar app.jar

# Configurar propriedades
COPY docker/application.properties .

# Definir usuário
USER bipuser

# Porta exposta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/api/v1/beneficios/status || exit 1

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Docker Compose (Desenvolvimento)
```yaml
version: '3.8'

services:
  bip-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=development
      - JVM_OPTS=-Xmx512m
    volumes:
      - ./logs:/app/logs
    networks:
      - bip-network

  bip-db:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=bipdb
      - POSTGRES_USER=bipuser
      - POSTGRES_PASSWORD=bippass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - bip-network

volumes:
  postgres_data:

networks:
  bip-network:
    driver: bridge
```

### 3. Comandos Docker
```bash
# Build da imagem
docker build -t bip-api:latest .

# Executar container
docker run -d \
  --name bip-api \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m" \
  bip-api:latest

# Com docker-compose
docker-compose up -d
```

---

## Deployment com Kubernetes

### 1. Deployment YAML
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: bip-api
  labels:
    app: bip-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: bip-api
  template:
    metadata:
      labels:
        app: bip-api
    spec:
      containers:
      - name: bip-api
        image: bip-api:latest
        ports:
        - containerPort: 8080
        env:
        - name: JAVA_OPTS
          value: "-Xmx512m -XX:+UseG1GC"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: bip-secrets
              key: database-url
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /api/v1/beneficios/status
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /api/v1/beneficios/status
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: bip-api-service
spec:
  selector:
    app: bip-api
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

### 2. ConfigMap para Configurações
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: bip-config
data:
  application.properties: |
    # Database Configuration
    javax.persistence.jdbc.driver=org.postgresql.Driver
    javax.persistence.jdbc.url=jdbc:postgresql://postgres:5432/bipdb
    
    # Hibernate Configuration
    hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    hibernate.hbm2ddl.auto=validate
    hibernate.show_sql=false
    
    # Connection Pool
    hibernate.connection.pool_size=10
    hibernate.connection.max_pool_size=20
```

### 3. Comandos Kubernetes
```bash
# Aplicar manifests
kubectl apply -f k8s/

# Verificar pods
kubectl get pods -l app=bip-api

# Logs
kubectl logs -f deployment/bip-api

# Escalar
kubectl scale deployment bip-api --replicas=5
```

---

## Deployment em Cloud

### 1. AWS (Amazon Web Services)

#### ECS (Elastic Container Service)
```json
{
  "family": "bip-api",
  "taskDefinition": {
    "cpu": "256",
    "memory": "512",
    "containerDefinitions": [
      {
        "name": "bip-api",
        "image": "your-account.dkr.ecr.region.amazonaws.com/bip-api:latest",
        "portMappings": [
          {
            "containerPort": 8080,
            "protocol": "tcp"
          }
        ],
        "environment": [
          {
            "name": "JAVA_OPTS",
            "value": "-Xmx400m"
          }
        ]
      }
    ]
  }
}
```

#### Elastic Beanstalk
```bash
# Criar pacote
mvn clean package

# Deploy via EB CLI
eb init bip-api
eb create bip-api-prod
eb deploy
```

### 2. Azure

#### Container Instances
```bash
# Deploy direto
az container create \
  --resource-group bip-rg \
  --name bip-api \
  --image bip-api:latest \
  --cpu 1 \
  --memory 1 \
  --ports 8080 \
  --environment-variables JAVA_OPTS="-Xmx512m"
```

#### App Service
```bash
# Usando Azure CLI
az webapp create \
  --resource-group bip-rg \
  --plan bip-plan \
  --name bip-api \
  --deployment-container-image-name bip-api:latest
```

### 3. Google Cloud

#### Cloud Run
```bash
# Deploy
gcloud run deploy bip-api \
  --image gcr.io/project-id/bip-api:latest \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 512Mi \
  --cpu 1
```

---

## Configurações por Ambiente

### 1. Desenvolvimento (development)
```properties
# H2 Database
javax.persistence.jdbc.driver=org.h2.Driver
javax.persistence.jdbc.url=jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1
hibernate.hbm2ddl.auto=create-drop
hibernate.show_sql=true

# Logs
logging.level.com.bip=DEBUG
```

### 2. Teste (testing)
```properties
# H2 for tests
javax.persistence.jdbc.driver=org.h2.Driver
javax.persistence.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
hibernate.hbm2ddl.auto=create-drop
hibernate.show_sql=false

# Test-specific configs
logging.level.com.bip=INFO
```

### 3. Produção (production)
```properties
# PostgreSQL
javax.persistence.jdbc.driver=org.postgresql.Driver
javax.persistence.jdbc.url=jdbc:postgresql://db:5432/bipdb
javax.persistence.jdbc.user=${DB_USER}
javax.persistence.jdbc.password=${DB_PASSWORD}

# Hibernate
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.hbm2ddl.auto=validate
hibernate.show_sql=false

# Pool
hibernate.connection.pool_size=20
hibernate.connection.max_pool_size=50

# Logs
logging.level.com.bip=WARN
logging.level.org.hibernate=WARN
```

---

## Monitoramento e Observabilidade

### 1. Health Checks
```bash
# Status da aplicação
curl http://localhost:8080/api/v1/beneficios/status

# Estatísticas
curl http://localhost:8080/api/v1/beneficios/estatisticas
```

### 2. Métricas
- **CPU**: Uso médio < 70%
- **Memória**: Uso médio < 80%
- **Response Time**: < 200ms (p95)
- **Error Rate**: < 1%

### 3. Logs Estruturados
```json
{
  "timestamp": "2025-01-24T12:30:45Z",
  "level": "INFO",
  "logger": "com.bip.presentation.controllers.BeneficioController",
  "message": "Benefício criado com sucesso",
  "beneficioId": 123,
  "userId": "user123"
}
```

---

## Troubleshooting

### Problemas Comuns

#### 1. Porta em Uso
```bash
# Verificar processo na porta 8080
netstat -tulpn | grep :8080
# ou
lsof -i :8080

# Matar processo
kill -9 <PID>
```

#### 2. Memória Insuficiente
```bash
# Aumentar heap size
export JAVA_OPTS="-Xmx1g -Xms512m"
java $JAVA_OPTS -jar app.jar
```

#### 3. Erro de Conectividade com BD
```bash
# Testar conexão
telnet db-host 5432

# Verificar logs
tail -f logs/application.log
```

### Scripts de Diagnóstico
```bash
#!/bin/bash
# health-check.sh
echo "=== BIP API Health Check ==="
echo "Timestamp: $(date)"
echo "Application Status:"
curl -s http://localhost:8080/api/v1/beneficios/status | jq .
echo -e "\nMemory Usage:"
free -h
echo -e "\nDisk Usage:"
df -h
```

---

## Backup e Recuperação

### 1. Backup de Dados
```sql
-- PostgreSQL
pg_dump -h localhost -U bipuser bipdb > backup_$(date +%Y%m%d).sql

-- MySQL
mysqldump -u bipuser -p bipdb > backup_$(date +%Y%m%d).sql
```

### 2. Estratégia de Backup
- **Frequência**: Diário (automático)
- **Retenção**: 30 dias
- **Localização**: Storage externo/cloud
- **Teste**: Restauração mensal

### 3. Plano de Recuperação
1. Identificar problema
2. Parar aplicação
3. Restaurar backup mais recente
4. Reiniciar aplicação
5. Validar funcionamento
6. Comunicar stakeholders

---

## Segurança

### 1. Configurações de Segurança
```properties
# HTTPS (produção)
server.ssl.enabled=true
server.ssl.key-store=keystore.p12
server.ssl.key-store-password=${SSL_PASSWORD}

# Headers de segurança
security.headers.frame-options=DENY
security.headers.xss-protection=1; mode=block
```

### 2. Variáveis de Ambiente Sensíveis
```bash
# Nunca hardcode em código
export DB_PASSWORD="secure-password"
export JWT_SECRET="super-secret-key"
export SSL_PASSWORD="keystore-password"
```

### 3. Scanning de Vulnerabilidades
```bash
# OWASP Dependency Check
mvn org.owasp:dependency-check-maven:check

# Snyk
snyk test

# Trivy (containers)
trivy image bip-api:latest
```