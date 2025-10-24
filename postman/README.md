# Postman Collections - BIP API

Esta pasta contém as collections do Postman para testes e validação da API BIP.

## Arquivos Incluídos

### 1. BIP-API-Collection.postman_collection.json
Collection completa com todos os endpoints da API:

- **Benefícios**
  - Listar todos os benefícios
  - Listar benefícios ativos
  - Buscar benefício por ID
  - Criar novo benefício
  - Atualizar benefício
  - Remover benefício
  - Status do sistema
  - Estatísticas

- **Transferências**
  - Executar transferência
  - Validar transferência
  - Calcular taxa

### 2. BIP-API-Development.postman_environment.json
Arquivo de ambiente para desenvolvimento com as seguintes variáveis:

- `baseUrl`: http://localhost:8080
- `apiVersion`: v1
- `contentType`: application/json

## Como Usar

### 1. Importar no Postman
1. Abra o Postman
2. Clique em "Import"
3. Selecione os arquivos `.json` desta pasta
4. A collection e ambiente serão importados automaticamente

### 2. Configurar Ambiente
1. No canto superior direito, selecione "BIP API - Development"
2. Verifique se as variáveis estão configuradas corretamente
3. Modifique `baseUrl` se necessário

### 3. Executar Requests
1. Certifique-se de que a API está rodando (`mvn jetty:run`)
2. Selecione um request na collection
3. Clique em "Send"
4. Verifique a resposta

## Collection Runner

Para executar todos os testes automaticamente:

1. Clique na collection "BIP API"
2. Selecione "Run collection"
3. Escolha o ambiente "BIP API - Development"
4. Clique em "Run BIP API"

## Testes Automatizados

Cada request inclui testes automatizados que verificam:

- Status code correto
- Estrutura da resposta
- Tipos de dados
- Validações de negócio

### Exemplos de Testes

```javascript
// Teste de status code
pm.test("Status code é 200", function () {
    pm.response.to.have.status(200);
});

// Teste de estrutura JSON
pm.test("Resposta contém propriedades obrigatórias", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('nome');
    pm.expect(jsonData).to.have.property('valor');
});

// Teste de tipo de dados
pm.test("ID é um número", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
});
```

## Variáveis Dinâmicas

A collection utiliza variáveis dinâmicas para testes em cadeia:

- `beneficioId`: ID do benefício criado/modificado
- `transferenceId`: ID da transferência
- `timestamp`: Timestamp atual para dados únicos

### Script de Setup
```javascript
// Gerar nome único para teste
const timestamp = Date.now();
pm.environment.set("timestamp", timestamp);
pm.environment.set("nomeBeneficio", `Teste ${timestamp}`);
```

## Documentação Automática

Para gerar documentação da API:

1. Selecione a collection
2. Clique em "View documentation"
3. Postman gerará documentação automaticamente
4. Clique em "Publish" para compartilhar

## Scripts de Teste

### Pre-request Scripts
Executados antes do request:

```javascript
// Gerar dados únicos
const timestamp = Date.now();
pm.environment.set("uniqueName", `Beneficio-${timestamp}`);

// Validar pré-condições
const beneficioId = pm.environment.get("beneficioId");
if (!beneficioId) {
    throw new Error("ID do benefício não encontrado");
}
```

### Test Scripts
Executados após o request:

```javascript
// Salvar ID para próximos requests
if (pm.response.code === 201) {
    const responseJson = pm.response.json();
    pm.environment.set("beneficioId", responseJson.id);
}

// Validar regras de negócio
pm.test("Valor deve ser positivo", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.valor).to.be.above(0);
});
```

## Troubleshooting

### Problemas Comuns

1. **Erro de Conexão**
   - Verificar se a API está rodando
   - Confirmar URL no ambiente
   - Testar conectividade: `curl http://localhost:8080/api/v1/beneficios/status`

2. **Testes Falhando**
   - Verificar se os dados de teste são únicos
   - Confirmar estrutura da resposta
   - Revisar logs da aplicação

3. **Variáveis Não Funcionando**
   - Verificar se o ambiente está selecionado
   - Confirmar nome das variáveis
   - Verificar scripts pre-request

### Debug de Requests

Para debugar requests:

```javascript
// Log da request
console.log("Request URL:", pm.request.url);
console.log("Request Body:", pm.request.body);

// Log da response
console.log("Response Status:", pm.response.code);
console.log("Response Body:", pm.response.text());
```

## Exportar Resultados

### Newman (CLI)
Para executar via linha de comando:

```bash
# Instalar Newman
npm install -g newman

# Executar collection
newman run BIP-API-Collection.postman_collection.json \
  -e BIP-API-Development.postman_environment.json \
  --reporters cli,json \
  --reporter-json-export results.json
```

### Integração CI/CD
Para integrar com pipelines:

```yaml
# GitHub Actions exemplo
- name: Run Postman Tests
  run: |
    newman run postman/BIP-API-Collection.postman_collection.json \
      -e postman/BIP-API-Development.postman_environment.json \
      --reporters junit \
      --reporter-junit-export newman-results.xml
```

## Versionamento

Mantenha as collections atualizadas:

1. Versione os arquivos no Git
2. Use tags para releases
3. Documente mudanças no CHANGELOG
4. Mantenha ambientes sincronizados

## Colaboração

Para trabalho em equipe:

1. **Postman Workspace**: Compartilhe workspace
2. **Git**: Versione collections no repositório
3. **Documentação**: Mantenha README atualizado
4. **Ambientes**: Use variáveis para diferentes ambientes

## Monitoramento

Configure monitoramento no Postman:

1. Selecione collection
2. Clique em "Monitor"
3. Configure frequência
4. Defina alertas
5. Acompanhe resultados

## Extensões

### Custom Scripts
```javascript
// Função para gerar CPF
function gerarCPF() {
    const random = () => Math.floor(Math.random() * 9);
    return `${random()}${random()}${random()}.${random()}${random()}${random()}.${random()}${random()}${random()}-${random()}${random()}`;
}

pm.globals.set("cpf", gerarCPF());
```

### Interceptor
Para capturar requests do browser:
1. Instale Postman Interceptor
2. Ative no Postman
3. Navegue pela aplicação
4. Requests serão capturados automaticamente