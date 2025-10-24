# 📦 Coleções Postman - BIP API

Este diretório contém as coleções do Postman para testar completamente a **BIP API**.

## 📁 Arquivos Incluídos

### 🔗 Coleção Principal
- **`BIP-API-Collection.postman_collection.json`**
  - Coleção completa com todos os endpoints
  - Cenários de teste organizados
  - Scripts de validação automática
  - Documentação integrada

### 🌍 Environment
- **`BIP-API-Development.postman_environment.json`**
  - Variáveis de ambiente para desenvolvimento
  - URLs e IDs configuráveis
  - Fácil troca entre ambientes

## 🚀 Como Usar

### 1. Importar no Postman
1. Abra o Postman
2. Clique em **Import**
3. Selecione os dois arquivos JSON
4. Confirme a importação

### 2. Configurar Environment
1. Selecione o environment **"BIP API - Development"**
2. Verifique se a variável `baseUrl` está correta: `http://localhost:8080/api`
3. Ajuste outras variáveis conforme necessário

### 3. Executar Testes
1. **Status da API**: Teste básico de conectividade
2. **Benefícios**: CRUD completo de benefícios
3. **Transferências**: Operações de transferência
4. **Cenários de Teste**: Fluxos completos e validações

## 📋 Endpoints Disponíveis

### 🏠 Status & Health Check
- `GET /beneficios/status` - Status da API e lista de endpoints

### 💰 Benefícios
- `GET /beneficios` - Listar todos os benefícios
- `GET /beneficios/ativos` - Listar apenas benefícios ativos
- `GET /beneficios/{id}` - Buscar benefício por ID
- `POST /beneficios` - Criar novo benefício
- `PUT /beneficios/{id}` - Atualizar benefício
- `DELETE /beneficios/{id}` - Remover benefício
- `GET /beneficios/estatisticas` - Estatísticas dos benefícios

### 🔄 Transferências
- `POST /transferencias` - Executar transferência
- `POST /transferencias/validar` - Validar transferência
- `GET /transferencias/taxa?valor={valor}` - Calcular taxa

## 🧪 Cenários de Teste

### 📝 Teste Completo - Criar e Transferir
Sequência completa que demonstra o fluxo da aplicação:
1. ✅ Criar benefício origem
2. ✅ Criar benefício destino  
3. ✅ Validar transferência
4. ✅ Executar transferência
5. ✅ Verificar saldos atualizados

### ⚠️ Teste de Validações
Testes para validar comportamento com dados inválidos:
- ❌ Benefício com dados inválidos
- ❌ Buscar benefício inexistente
- ❌ Transferência com saldo insuficiente
- ❌ Transferência para mesmo benefício

## 🔧 Variáveis de Environment

| Variável | Valor Padrão | Descrição |
|----------|--------------|-----------|
| `baseUrl` | `http://localhost:8080/api` | URL base da API |
| `serverHost` | `localhost` | Host do servidor |
| `serverPort` | `8080` | Porta do servidor |
| `beneficioId` | `1` | ID de benefício para testes |
| `beneficioOrigemId` | `1` | ID do benefício origem |
| `beneficioDestinoId` | `2` | ID do benefício destino |
| `valorTransferencia` | `100.00` | Valor padrão para transferências |

## 📊 Scripts de Teste Automático

### 🔍 Testes Globais (executados em todas as requisições)
- ✅ Status code válido (200, 201, 204, 400, 404, 500)
- ⏱️ Response time < 5 segundos
- 📄 Content-Type é JSON (quando aplicável)
- 📝 Logs automáticos de requisição e resposta

### 🎯 Validações Específicas
- Estrutura de dados de resposta
- Campos obrigatórios presentes
- Tipos de dados corretos
- Códigos de erro apropriados

## 📚 Exemplos de Uso

### Criar um Benefício
```json
POST /api/beneficios
{
  "nome": "Vale Alimentação",
  "descricao": "Benefício para compra de alimentos",
  "valorInicial": 500.00
}
```

### Executar Transferência
```json
POST /api/transferencias
{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00,
  "descricao": "Transferência entre benefícios"
}
```

## 🛡️ Validações Implementadas

### Benefícios
- **Nome**: 3-100 caracteres (obrigatório)
- **Descrição**: até 500 caracteres
- **Valor Inicial**: ≥ 0 (obrigatório)

### Transferências
- **IDs**: números positivos (obrigatórios)
- **Valor**: > 0.01 (obrigatório)
- **Descrição**: até 500 caracteres
- **Regras de Negócio**:
  - Benefícios devem existir e estar ativos
  - Saldo suficiente na origem
  - Origem ≠ Destino

## 🐛 Troubleshooting

### Servidor não responde
1. ✅ Verifique se o servidor está rodando: `mvn jetty:run`
2. ✅ Confirme a porta 8080 está livre
3. ✅ Teste: `curl http://localhost:8080/api/beneficios/status`

### Erros de validação
1. ✅ Verifique os dados de entrada
2. ✅ Consulte a documentação dos DTOs
3. ✅ Veja os logs do servidor para detalhes

### Environment não carrega
1. ✅ Reimporte o arquivo de environment
2. ✅ Selecione o environment correto
3. ✅ Verifique as variáveis estão ativas

## 📞 Suporte

Para dúvidas ou problemas:
1. 📖 Consulte a documentação da API
2. 🔍 Verifique os logs do servidor
3. 🧪 Execute os testes de validação
4. 💬 Entre em contato com a equipe BIP

---

**🎯 Última atualização:** 24/10/2025  
**🔗 Versão da API:** 3.0.0-Clean-Architecture  
**🛠️ Postman:** v10.20.0+