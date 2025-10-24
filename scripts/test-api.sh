#!/bin/bash

# ========================================
# BIP API - Scripts de Teste cURL
# ========================================
# Conjunto completo de comandos para testar
# todos os endpoints da BIP API via cURL
# ========================================

# Configurações
BASE_URL="http://localhost:8080/api"
CONTENT_TYPE="Content-Type: application/json"
ACCEPT="Accept: application/json"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para exibir headers
print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

# Função para exibir comandos
print_command() {
    echo -e "${YELLOW}Comando:${NC} $1\n"
}

# Função para pausar entre comandos
pause_between_commands() {
    echo -e "\n${GREEN}Pressione Enter para continuar...${NC}"
    read
}

# ========================================
# 1. STATUS E HEALTH CHECK
# ========================================

print_header "1. STATUS DA API"

print_command "curl -X GET \"$BASE_URL/beneficios/status\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios/status" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# ========================================
# 2. BENEFÍCIOS - CRUD COMPLETO
# ========================================

print_header "2. BENEFÍCIOS - OPERAÇÕES CRUD"

# 2.1 Listar todos os benefícios
echo -e "${GREEN}2.1 Listar todos os benefícios${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# 2.2 Criar novo benefício
echo -e "${GREEN}2.2 Criar novo benefício${NC}"
CRIAR_BENEFICIO='{
  "nome": "Vale Alimentação",
  "descricao": "Benefício para compra de alimentos",
  "valorInicial": 500.00
}'

print_command "curl -X POST \"$BASE_URL/beneficios\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$CRIAR_BENEFICIO'"
curl -X POST "$BASE_URL/beneficios" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$CRIAR_BENEFICIO" | jq '.'

pause_between_commands

# 2.3 Criar segundo benefício para transferências
echo -e "${GREEN}2.3 Criar segundo benefício${NC}"
CRIAR_BENEFICIO_2='{
  "nome": "Vale Transporte",
  "descricao": "Benefício para transporte público",
  "valorInicial": 200.00
}'

print_command "curl -X POST \"$BASE_URL/beneficios\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$CRIAR_BENEFICIO_2'"
curl -X POST "$BASE_URL/beneficios" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$CRIAR_BENEFICIO_2" | jq '.'

pause_between_commands

# 2.4 Buscar benefício por ID
echo -e "${GREEN}2.4 Buscar benefício por ID (ID=1)${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios/1\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios/1" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# 2.5 Listar benefícios ativos
echo -e "${GREEN}2.5 Listar benefícios ativos${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios/ativos\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios/ativos" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# 2.6 Atualizar benefício
echo -e "${GREEN}2.6 Atualizar benefício (ID=1)${NC}"
ATUALIZAR_BENEFICIO='{
  "nome": "Vale Alimentação Premium",
  "descricao": "Benefício premium para compra de alimentos orgânicos",
  "valorInicial": 750.00
}'

print_command "curl -X PUT \"$BASE_URL/beneficios/1\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$ATUALIZAR_BENEFICIO'"
curl -X PUT "$BASE_URL/beneficios/1" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$ATUALIZAR_BENEFICIO" | jq '.'

pause_between_commands

# 2.7 Estatísticas dos benefícios
echo -e "${GREEN}2.7 Estatísticas dos benefícios${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios/estatisticas\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios/estatisticas" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# ========================================
# 3. TRANSFERÊNCIAS
# ========================================

print_header "3. TRANSFERÊNCIAS ENTRE BENEFÍCIOS"

# 3.1 Calcular taxa
echo -e "${GREEN}3.1 Calcular taxa para transferência de R$ 100,00${NC}"
print_command "curl -X GET \"$BASE_URL/transferencias/taxa?valor=100.00\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/transferencias/taxa?valor=100.00" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# 3.2 Validar transferência
echo -e "${GREEN}3.2 Validar transferência (sem executar)${NC}"
VALIDAR_TRANSFERENCIA='{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00,
  "descricao": "Validação de transferência"
}'

print_command "curl -X POST \"$BASE_URL/transferencias/validar\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$VALIDAR_TRANSFERENCIA'"
curl -X POST "$BASE_URL/transferencias/validar" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$VALIDAR_TRANSFERENCIA" | jq '.'

pause_between_commands

# 3.3 Executar transferência
echo -e "${GREEN}3.3 Executar transferência${NC}"
EXECUTAR_TRANSFERENCIA='{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 2,
  "valor": 100.00,
  "descricao": "Transferência de teste via cURL"
}'

print_command "curl -X POST \"$BASE_URL/transferencias\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$EXECUTAR_TRANSFERENCIA'"
curl -X POST "$BASE_URL/transferencias" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$EXECUTAR_TRANSFERENCIA" | jq '.'

pause_between_commands

# 3.4 Verificar saldos após transferência
echo -e "${GREEN}3.4 Verificar saldos após transferência${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# ========================================
# 4. TESTES DE VALIDAÇÃO (CASOS NEGATIVOS)
# ========================================

print_header "4. TESTES DE VALIDAÇÃO - CASOS NEGATIVOS"

# 4.1 Benefício com dados inválidos
echo -e "${GREEN}4.1 Teste: Criar benefício com dados inválidos${NC}"
BENEFICIO_INVALIDO='{
  "nome": "AB",
  "descricao": "",
  "valorInicial": -100.00
}'

print_command "curl -X POST \"$BASE_URL/beneficios\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$BENEFICIO_INVALIDO'"
curl -X POST "$BASE_URL/beneficios" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$BENEFICIO_INVALIDO" | jq '.'

pause_between_commands

# 4.2 Buscar benefício inexistente
echo -e "${GREEN}4.2 Teste: Buscar benefício inexistente (ID=99999)${NC}"
print_command "curl -X GET \"$BASE_URL/beneficios/99999\" -H \"$ACCEPT\""
curl -X GET "$BASE_URL/beneficios/99999" \
     -H "$ACCEPT" | jq '.'

pause_between_commands

# 4.3 Transferência com saldo insuficiente
echo -e "${GREEN}4.3 Teste: Transferência com saldo insuficiente${NC}"
TRANSFERENCIA_SALDO_INSUFICIENTE='{
  "beneficioOrigemId": 2,
  "beneficioDestinoId": 1,
  "valor": 999999.00,
  "descricao": "Teste de saldo insuficiente"
}'

print_command "curl -X POST \"$BASE_URL/transferencias\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$TRANSFERENCIA_SALDO_INSUFICIENTE'"
curl -X POST "$BASE_URL/transferencias" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$TRANSFERENCIA_SALDO_INSUFICIENTE" | jq '.'

pause_between_commands

# 4.4 Transferência para o mesmo benefício
echo -e "${GREEN}4.4 Teste: Transferência para o mesmo benefício${NC}"
TRANSFERENCIA_MESMO_BENEFICIO='{
  "beneficioOrigemId": 1,
  "beneficioDestinoId": 1,
  "valor": 50.00,
  "descricao": "Teste de transferência para o mesmo benefício"
}'

print_command "curl -X POST \"$BASE_URL/transferencias\" -H \"$CONTENT_TYPE\" -H \"$ACCEPT\" -d '$TRANSFERENCIA_MESMO_BENEFICIO'"
curl -X POST "$BASE_URL/transferencias" \
     -H "$CONTENT_TYPE" \
     -H "$ACCEPT" \
     -d "$TRANSFERENCIA_MESMO_BENEFICIO" | jq '.'

pause_between_commands

# ========================================
# 5. LIMPEZA (OPCIONAL)
# ========================================

print_header "5. LIMPEZA - REMOVER BENEFÍCIOS DE TESTE"

echo -e "${RED}ATENÇÃO: As próximas operações irão remover os benefícios criados para teste.${NC}"
echo -e "${YELLOW}Deseja continuar? (y/N)${NC}"
read -r CONFIRM

if [[ $CONFIRM == "y" || $CONFIRM == "Y" ]]; then
    # 5.1 Remover benefício 1
    echo -e "${GREEN}5.1 Remover benefício ID=1${NC}"
    print_command "curl -X DELETE \"$BASE_URL/beneficios/1\" -H \"$ACCEPT\""
    curl -X DELETE "$BASE_URL/beneficios/1" \
         -H "$ACCEPT" | jq '.'

    pause_between_commands

    # 5.2 Remover benefício 2
    echo -e "${GREEN}5.2 Remover benefício ID=2${NC}"
    print_command "curl -X DELETE \"$BASE_URL/beneficios/2\" -H \"$ACCEPT\""
    curl -X DELETE "$BASE_URL/beneficios/2" \
         -H "$ACCEPT" | jq '.'

    pause_between_commands

    # 5.3 Verificar se foram removidos
    echo -e "${GREEN}5.3 Verificar lista final de benefícios${NC}"
    print_command "curl -X GET \"$BASE_URL/beneficios\" -H \"$ACCEPT\""
    curl -X GET "$BASE_URL/beneficios" \
         -H "$ACCEPT" | jq '.'
else
    echo -e "${YELLOW}Limpeza cancelada. Benefícios mantidos no sistema.${NC}"
fi

# ========================================
# CONCLUSÃO
# ========================================

print_header "TESTES CONCLUÍDOS"

echo -e "${GREEN}✅ Todos os testes foram executados com sucesso!${NC}"
echo -e "${BLUE}📊 Resumo dos testes realizados:${NC}"
echo -e "   • Status da API"
echo -e "   • CRUD completo de benefícios"
echo -e "   • Operações de transferência"
echo -e "   • Validações e casos negativos"
echo -e "   • Limpeza (opcional)"
echo -e "\n${YELLOW}🔗 Para mais informações, consulte:${NC}"
echo -e "   • README.md"
echo -e "   • POSTMAN-COLLECTIONS.md"
echo -e "   • Coleções do Postman"

echo -e "\n${GREEN}🎯 API BIP testada com sucesso!${NC}\n"