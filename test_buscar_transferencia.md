# Teste do Endpoint buscarPorId - Transferências

## Endpoint Implementado
`GET /api/transferencias/{id}`

## Descrição
Foi implementado o método `buscarPorId` no `TransferenciaController` que permite buscar uma transferência específica por ID.

## Funcionalidades Adicionadas

### 1. TransferenciaUseCase
- Método `buscarPorId(Long id)` - busca uma transferência por ID
- Validação de ID (deve ser positivo)
- Retorna `HistoricoTransferenciaDto` ou `null` se não encontrado

### 2. TransferenciaController
- Endpoint `GET /api/transferencias/{id}`
- Validação de parâmetros de entrada
- Tratamento de erros (ID inválido, transferência não encontrada)
- Resposta padronizada usando `ErrorResponseBuilder`

### 3. Documentação Atualizada
- Lista de endpoints no método `getStatus()` foi atualizada
- Inclui o novo endpoint `GET /api/transferencias/{id}`

## Testes Manuais Possíveis

### Teste 1: Buscar transferência existente
```bash
curl -X GET "http://localhost:8080/api/transferencias/1" \
     -H "Accept: application/json"
```

### Teste 2: Buscar transferência inexistente
```bash
curl -X GET "http://localhost:8080/api/transferencias/999" \
     -H "Accept: application/json"
```

### Teste 3: ID inválido (negativo)
```bash
curl -X GET "http://localhost:8080/api/transferencias/-1" \
     -H "Accept: application/json"
```

### Teste 4: ID inválido (zero)
```bash
curl -X GET "http://localhost:8080/api/transferencias/0" \
     -H "Accept: application/json"
```

### Teste 5: Verificar status da API
```bash
curl -X GET "http://localhost:8080/api/transferencias/status" \
     -H "Accept: application/json"
```

## Respostas Esperadas

### Transferência encontrada (200 OK):
```json
{
  "id": 1,
  "beneficioOrigemId": 1,
  "beneficioOrigemNome": "Auxílio Alimentação",
  "beneficioDestinoId": 2,
  "beneficioDestinoNome": "Vale Transporte",
  "valor": 100.00,
  "taxa": 1.00,
  "descricao": "Transferência de exemplo",
  "dataExecucao": "2025-10-24T01:59:22",
  "status": "CONCLUIDA"
}
```

### Transferência não encontrada (404 Not Found):
```json
{
  "erro": "Transferência não encontrada",
  "id": 999
}
```

### ID inválido (400 Bad Request):
```json
{
  "erro": "ID deve ser um número positivo"
}
```

## Padrão Implementado
O endpoint segue o mesmo padrão do `BeneficioController.buscarPorId()`:
- Validação de entrada
- Tratamento de exceções
- Respostas HTTP apropriadas
- Uso do `ErrorResponseBuilder` para padronização

## Observações
- Como o sistema usa dados simulados (não há persistência real), apenas alguns IDs de exemplo (1, 2, 3) retornarão transferências
- Em um cenário real, este método consultaria uma tabela de histórico de transferências no banco de dados
- A implementação está preparada para uma futura migração para persistência real