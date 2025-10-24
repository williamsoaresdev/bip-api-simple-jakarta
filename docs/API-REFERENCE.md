# API Reference - BIP API

## Base URL
```
http://localhost:8080/api/v1
```

## Content Type
```
Content-Type: application/json
Accept: application/json
```

---

## Benefícios

### 1. Listar Todos os Benefícios
```http
GET /beneficios
```

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "nome": "Auxilio Alimentacao",
        "descricao": "Beneficio para alimentacao",
        "valor": 500.00,
        "ativo": true,
        "criadoEm": "2025-01-24T09:26:49",
        "atualizadoEm": "2025-01-24T09:26:49",
        "versao": 0
    }
]
```

### 2. Listar Benefícios Ativos
```http
GET /beneficios/ativos
```

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "nome": "Auxilio Alimentacao",
        "descricao": "Beneficio para alimentacao",
        "valor": 500.00,
        "ativo": true,
        "criadoEm": "2025-01-24T09:26:49",
        "atualizadoEm": "2025-01-24T09:26:49",
        "versao": 0
    }
]
```

### 3. Buscar Benefício por ID
```http
GET /beneficios/{id}
```

**Path Parameters:**
- `id` (Long): ID do benefício

**Response:** `200 OK`
```json
{
    "id": 1,
    "nome": "Auxilio Alimentacao",
    "descricao": "Beneficio para alimentacao",
    "valor": 500.00,
    "ativo": true,
    "criadoEm": "2025-01-24T09:26:49",
    "atualizadoEm": "2025-01-24T09:26:49",
    "versao": 0
}
```

**Error Response:** `404 Not Found`
```json
{
    "erro": "Benefício não encontrado",
    "codigo": "BENEFICIO_NAO_ENCONTRADO",
    "timestamp": "2025-01-24T12:30:45",
    "path": "/api/v1/beneficios/999"
}
```

### 4. Criar Novo Benefício
```http
POST /beneficios
```

**Request Body:**
```json
{
    "nome": "Vale Cultura",
    "descricao": "Beneficio para atividades culturais",
    "valor": 100.00,
    "ativo": true
}
```

**Response:** `201 Created`
```json
{
    "id": 4,
    "nome": "Vale Cultura",
    "descricao": "Beneficio para atividades culturais",
    "valor": 100.00,
    "ativo": true,
    "criadoEm": "2025-01-24T12:30:45",
    "atualizadoEm": "2025-01-24T12:30:45",
    "versao": 0
}
```

**Error Response:** `400 Bad Request`
```json
{
    "erro": "Nome do benefício já existe",
    "codigo": "NOME_JA_EXISTE",
    "timestamp": "2025-01-24T12:30:45",
    "path": "/api/v1/beneficios"
}
```

### 5. Atualizar Benefício
```http
PUT /beneficios/{id}
```

**Path Parameters:**
- `id` (Long): ID do benefício

**Request Body:**
```json
{
    "nome": "Vale Cultura Atualizado",
    "descricao": "Beneficio para atividades culturais - Atualizado",
    "valor": 150.00,
    "ativo": true,
    "versao": 0
}
```

**Response:** `200 OK`
```json
{
    "id": 4,
    "nome": "Vale Cultura Atualizado",
    "descricao": "Beneficio para atividades culturais - Atualizado",
    "valor": 150.00,
    "ativo": true,
    "criadoEm": "2025-01-24T12:30:45",
    "atualizadoEm": "2025-01-24T12:35:22",
    "versao": 1
}
```

### 6. Remover Benefício
```http
DELETE /beneficios/{id}
```

**Path Parameters:**
- `id` (Long): ID do benefício

**Response:** `204 No Content`

**Error Response:** `404 Not Found`
```json
{
    "erro": "Benefício não encontrado",
    "codigo": "BENEFICIO_NAO_ENCONTRADO",
    "timestamp": "2025-01-24T12:30:45",
    "path": "/api/v1/beneficios/999"
}
```

### 7. Status do Sistema
```http
GET /beneficios/status
```

**Response:** `200 OK`
```json
{
    "status": "Sistema Operacional",
    "timestamp": "2025-01-24T12:30:45",
    "versao": "1.0.0-SNAPSHOT",
    "ambiente": "development"
}
```

### 8. Estatísticas
```http
GET /beneficios/estatisticas
```

**Response:** `200 OK`
```json
{
    "totalBeneficios": 4,
    "beneficiosAtivos": 3,
    "beneficiosInativos": 1,
    "valorTotalAtivos": 1750.00,
    "ultimaAtualizacao": "2025-01-24T12:30:45"
}
```

---

## Transferências

### 1. Executar Transferência
```http
POST /transferencias
```

**Request Body:**
```json
{
    "beneficioOrigemId": 1,
    "beneficioDestinoId": 2,
    "valor": 100.00,
    "observacao": "Transferencia para vale transporte"
}
```

**Response:** `200 OK`
```json
{
    "id": "TXN-20250124-001",
    "beneficioOrigemId": 1,
    "beneficioDestinoId": 2,
    "valor": 100.00,
    "taxa": 5.00,
    "valorLiquido": 95.00,
    "observacao": "Transferencia para vale transporte",
    "status": "CONCLUIDA",
    "dataProcessamento": "2025-01-24T12:30:45"
}
```

**Error Response:** `400 Bad Request`
```json
{
    "erro": "Saldo insuficiente para transferência",
    "codigo": "SALDO_INSUFICIENTE",
    "timestamp": "2025-01-24T12:30:45",
    "path": "/api/v1/transferencias"
}
```

### 2. Validar Transferência
```http
POST /transferencias/validar
```

**Request Body:**
```json
{
    "beneficioOrigemId": 1,
    "beneficioDestinoId": 2,
    "valor": 100.00
}
```

**Response:** `200 OK`
```json
{
    "valida": true,
    "taxa": 5.00,
    "valorLiquido": 95.00,
    "observacoes": [
        "Transferência válida",
        "Taxa de 5% será aplicada"
    ]
}
```

**Error Response:** `400 Bad Request`
```json
{
    "valida": false,
    "erros": [
        "Benefício de origem não encontrado",
        "Valor deve ser maior que zero"
    ]
}
```

### 3. Calcular Taxa
```http
GET /transferencias/taxa?valor={valor}
```

**Query Parameters:**
- `valor` (BigDecimal): Valor da transferência

**Response:** `200 OK`
```json
{
    "valorOriginal": 100.00,
    "taxa": 5.00,
    "percentualTaxa": 5.0,
    "valorLiquido": 95.00
}
```

---

## Códigos de Status HTTP

| Status | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Operação bem-sucedida sem conteúdo |
| 400 | Bad Request - Dados inválidos na requisição |
| 404 | Not Found - Recurso não encontrado |
| 409 | Conflict - Conflito de dados (ex: nome duplicado) |
| 500 | Internal Server Error - Erro interno do servidor |

## Códigos de Erro Personalizados

| Código | Descrição |
|---------|-----------|
| BENEFICIO_NAO_ENCONTRADO | Benefício não existe no sistema |
| NOME_JA_EXISTE | Nome do benefício já está em uso |
| SALDO_INSUFICIENTE | Valor insuficiente para operação |
| VALOR_INVALIDO | Valor deve ser maior que zero |
| VERSAO_CONFLITO | Versão do recurso está desatualizada |
| TRANSFERENCIA_INVALIDA | Dados da transferência são inválidos |

## Validações

### Benefício
- **nome**: Obrigatório, máximo 100 caracteres, único
- **descricao**: Opcional, máximo 500 caracteres
- **valor**: Obrigatório, maior que zero, máximo 2 casas decimais
- **ativo**: Obrigatório, booleano

### Transferência
- **beneficioOrigemId**: Obrigatório, deve existir e estar ativo
- **beneficioDestinoId**: Obrigatório, deve existir, diferente da origem
- **valor**: Obrigatório, maior que zero, máximo 2 casas decimais
- **observacao**: Opcional, máximo 255 caracteres

## Rate Limiting
Atualmente não implementado, mas recomendado para produção:
- 100 requisições por minuto por IP
- 1000 requisições por hora por IP

## Headers Recomendados

### Request
```http
Content-Type: application/json
Accept: application/json
User-Agent: BIP-Client/1.0
```

### Response
```http
Content-Type: application/json
X-Response-Time: 150ms
X-API-Version: 1.0.0
```