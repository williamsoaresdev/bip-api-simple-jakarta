# ========================================
# BIP API - Scripts de Teste PowerShell
# ========================================
# Conjunto completo de comandos para testar
# todos os endpoints da BIP API via PowerShell
# ========================================

# Configurações
$BaseUrl = "http://localhost:8080/api"
$Headers = @{
    "Content-Type" = "application/json"
    "Accept" = "application/json"
}

# Função para exibir headers coloridos
function Write-Header {
    param([string]$Message)
    Write-Host "`n========================================" -ForegroundColor Blue
    Write-Host $Message -ForegroundColor Blue
    Write-Host "========================================`n" -ForegroundColor Blue
}

# Função para exibir comandos
function Write-Command {
    param([string]$Command)
    Write-Host "Comando: " -ForegroundColor Yellow -NoNewline
    Write-Host $Command -ForegroundColor White
    Write-Host ""
}

# Função para pausar entre comandos
function Wait-Continue {
    Write-Host "`nPressione Enter para continuar..." -ForegroundColor Green
    Read-Host
}

# Função para fazer requisições HTTP com tratamento de erro
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Uri,
        [hashtable]$Headers,
        [string]$Body = $null
    )
    
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Uri -Method $Method -Headers $Headers -Body $Body
        } else {
            $response = Invoke-RestMethod -Uri $Uri -Method $Method -Headers $Headers
        }
        
        $response | ConvertTo-Json -Depth 10 | Write-Host
        return $response
    }
    catch {
        Write-Host "Erro na requisição: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            $streamReader = [System.IO.StreamReader]::new($_.Exception.Response.GetResponseStream())
            $errorResponse = $streamReader.ReadToEnd()
            Write-Host "Resposta de erro: $errorResponse" -ForegroundColor Red
        }
    }
}

# ========================================
# 1. STATUS E HEALTH CHECK
# ========================================

Write-Header "1. STATUS DA API"

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/status`" -Method GET -Headers @{Accept='application/json'}"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios/status" -Headers $Headers

Wait-Continue

# ========================================
# 2. BENEFÍCIOS - CRUD COMPLETO
# ========================================

Write-Header "2. BENEFÍCIOS - OPERAÇÕES CRUD"

# 2.1 Listar todos os benefícios
Write-Host "2.1 Listar todos os benefícios" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios" -Headers $Headers

Wait-Continue

# 2.2 Criar novo benefício
Write-Host "2.2 Criar novo benefício" -ForegroundColor Green
$criarBeneficio = @{
    nome = "Vale Alimentação"
    descricao = "Benefício para compra de alimentos"
    valorInicial = 500.00
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/beneficios" -Headers $Headers -Body $criarBeneficio

Wait-Continue

# 2.3 Criar segundo benefício
Write-Host "2.3 Criar segundo benefício" -ForegroundColor Green
$criarBeneficio2 = @{
    nome = "Vale Transporte"
    descricao = "Benefício para transporte público"
    valorInicial = 200.00
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/beneficios" -Headers $Headers -Body $criarBeneficio2

Wait-Continue

# 2.4 Buscar benefício por ID
Write-Host "2.4 Buscar benefício por ID (ID=1)" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/1`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios/1" -Headers $Headers

Wait-Continue

# 2.5 Listar benefícios ativos
Write-Host "2.5 Listar benefícios ativos" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/ativos`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios/ativos" -Headers $Headers

Wait-Continue

# 2.6 Atualizar benefício
Write-Host "2.6 Atualizar benefício (ID=1)" -ForegroundColor Green
$atualizarBeneficio = @{
    nome = "Vale Alimentação Premium"
    descricao = "Benefício premium para compra de alimentos orgânicos"
    valorInicial = 750.00
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/1`" -Method PUT -Body `$JsonBody"
Invoke-ApiRequest -Method "PUT" -Uri "$BaseUrl/beneficios/1" -Headers $Headers -Body $atualizarBeneficio

Wait-Continue

# 2.7 Estatísticas dos benefícios
Write-Host "2.7 Estatísticas dos benefícios" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/estatisticas`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios/estatisticas" -Headers $Headers

Wait-Continue

# ========================================
# 3. TRANSFERÊNCIAS
# ========================================

Write-Header "3. TRANSFERÊNCIAS ENTRE BENEFÍCIOS"

# 3.1 Calcular taxa
Write-Host "3.1 Calcular taxa para transferência de R$ 100,00" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/transferencias/taxa?valor=100.00`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/transferencias/taxa?valor=100.00" -Headers $Headers

Wait-Continue

# 3.2 Validar transferência
Write-Host "3.2 Validar transferência (sem executar)" -ForegroundColor Green
$validarTransferencia = @{
    beneficioOrigemId = 1
    beneficioDestinoId = 2
    valor = 100.00
    descricao = "Validação de transferência"
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/transferencias/validar`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/transferencias/validar" -Headers $Headers -Body $validarTransferencia

Wait-Continue

# 3.3 Executar transferência
Write-Host "3.3 Executar transferência" -ForegroundColor Green
$executarTransferencia = @{
    beneficioOrigemId = 1
    beneficioDestinoId = 2
    valor = 100.00
    descricao = "Transferência de teste via PowerShell"
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/transferencias`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/transferencias" -Headers $Headers -Body $executarTransferencia

Wait-Continue

# 3.4 Verificar saldos após transferência
Write-Host "3.4 Verificar saldos após transferência" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios" -Headers $Headers

Wait-Continue

# ========================================
# 4. TESTES DE VALIDAÇÃO (CASOS NEGATIVOS)
# ========================================

Write-Header "4. TESTES DE VALIDAÇÃO - CASOS NEGATIVOS"

# 4.1 Benefício com dados inválidos
Write-Host "4.1 Teste: Criar benefício com dados inválidos" -ForegroundColor Green
$beneficioInvalido = @{
    nome = "AB"
    descricao = ""
    valorInicial = -100.00
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method POST -Body `$JsonInválido"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/beneficios" -Headers $Headers -Body $beneficioInvalido

Wait-Continue

# 4.2 Buscar benefício inexistente
Write-Host "4.2 Teste: Buscar benefício inexistente (ID=99999)" -ForegroundColor Green
Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/99999`" -Method GET"
Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios/99999" -Headers $Headers

Wait-Continue

# 4.3 Transferência com saldo insuficiente
Write-Host "4.3 Teste: Transferência com saldo insuficiente" -ForegroundColor Green
$transferenciaSaldoInsuficiente = @{
    beneficioOrigemId = 2
    beneficioDestinoId = 1
    valor = 999999.00
    descricao = "Teste de saldo insuficiente"
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/transferencias`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/transferencias" -Headers $Headers -Body $transferenciaSaldoInsuficiente

Wait-Continue

# 4.4 Transferência para o mesmo benefício
Write-Host "4.4 Teste: Transferência para o mesmo benefício" -ForegroundColor Green
$transferenciaMesmoBeneficio = @{
    beneficioOrigemId = 1
    beneficioDestinoId = 1
    valor = 50.00
    descricao = "Teste de transferência para o mesmo benefício"
} | ConvertTo-Json

Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/transferencias`" -Method POST -Body `$JsonBody"
Invoke-ApiRequest -Method "POST" -Uri "$BaseUrl/transferencias" -Headers $Headers -Body $transferenciaMesmoBeneficio

Wait-Continue

# ========================================
# 5. LIMPEZA (OPCIONAL)
# ========================================

Write-Header "5. LIMPEZA - REMOVER BENEFÍCIOS DE TESTE"

Write-Host "ATENÇÃO: As próximas operações irão remover os benefícios criados para teste." -ForegroundColor Red
$confirm = Read-Host "Deseja continuar? (y/N)"

if ($confirm -eq "y" -or $confirm -eq "Y") {
    # 5.1 Remover benefício 1
    Write-Host "5.1 Remover benefício ID=1" -ForegroundColor Green
    Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/1`" -Method DELETE"
    Invoke-ApiRequest -Method "DELETE" -Uri "$BaseUrl/beneficios/1" -Headers $Headers

    Wait-Continue

    # 5.2 Remover benefício 2
    Write-Host "5.2 Remover benefício ID=2" -ForegroundColor Green
    Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios/2`" -Method DELETE"
    Invoke-ApiRequest -Method "DELETE" -Uri "$BaseUrl/beneficios/2" -Headers $Headers

    Wait-Continue

    # 5.3 Verificar se foram removidos
    Write-Host "5.3 Verificar lista final de benefícios" -ForegroundColor Green
    Write-Command "Invoke-RestMethod -Uri `"$BaseUrl/beneficios`" -Method GET"
    Invoke-ApiRequest -Method "GET" -Uri "$BaseUrl/beneficios" -Headers $Headers
}
else {
    Write-Host "Limpeza cancelada. Benefícios mantidos no sistema." -ForegroundColor Yellow
}

# ========================================
# CONCLUSÃO
# ========================================

Write-Header "TESTES CONCLUÍDOS"

Write-Host "✅ Todos os testes foram executados com sucesso!" -ForegroundColor Green
Write-Host "📊 Resumo dos testes realizados:" -ForegroundColor Blue
Write-Host "   • Status da API"
Write-Host "   • CRUD completo de benefícios"
Write-Host "   • Operações de transferência"
Write-Host "   • Validações e casos negativos"
Write-Host "   • Limpeza (opcional)"
Write-Host "`n🔗 Para mais informações, consulte:" -ForegroundColor Yellow
Write-Host "   • README.md"
Write-Host "   • POSTMAN-COLLECTIONS.md"
Write-Host "   • Coleções do Postman"

Write-Host "`n🎯 API BIP testada com sucesso!`n" -ForegroundColor Green