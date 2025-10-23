#!/bin/bash

# ============================================
# BIP API - Script de Análise de Qualidade
# ============================================

echo "🔧 Iniciando análise completa de qualidade de código..."
echo ""

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para mostrar status
show_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
        exit 1
    fi
}

# Função para mostrar informação
show_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Função para mostrar warning
show_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Limpar compilações anteriores
echo -e "${BLUE}🧹 Limpando compilações anteriores...${NC}"
mvn clean > /dev/null 2>&1
show_status $? "Limpeza concluída"

# Compilar o projeto
echo -e "${BLUE}🔨 Compilando o projeto...${NC}"
mvn compile -q
show_status $? "Compilação concluída"

# Executar testes
echo -e "${BLUE}🧪 Executando testes unitários...${NC}"
mvn test -q
TESTS_RESULT=$?
if [ $TESTS_RESULT -eq 0 ]; then
    TEST_COUNT=$(grep -o "Tests run: [0-9]*" target/surefire-reports/*.txt | head -1 | grep -o "[0-9]*" | head -1)
    show_status $TESTS_RESULT "Testes executados com sucesso ($TEST_COUNT testes)"
else
    show_status $TESTS_RESULT "Falha nos testes unitários"
fi

# CheckStyle
echo -e "${BLUE}✅ Executando CheckStyle...${NC}"
mvn checkstyle:check -q
CHECKSTYLE_RESULT=$?
if [ $CHECKSTYLE_RESULT -eq 0 ]; then
    show_status $CHECKSTYLE_RESULT "CheckStyle passou sem violações"
else
    show_warning "CheckStyle encontrou violações (verificar target/checkstyle-result.xml)"
fi

# PMD
echo -e "${BLUE}🔍 Executando PMD...${NC}"
mvn pmd:check -q
PMD_RESULT=$?
if [ $PMD_RESULT -eq 0 ]; then
    show_status $PMD_RESULT "PMD passou sem problemas"
else
    show_warning "PMD encontrou issues (verificar target/pmd.xml)"
fi

# CPD (Copy-Paste Detector)
echo -e "${BLUE}📋 Executando CPD (detecção de duplicação)...${NC}"
mvn pmd:cpd-check -q
CPD_RESULT=$?
if [ $CPD_RESULT -eq 0 ]; then
    show_status $CPD_RESULT "CPD não encontrou duplicações"
else
    show_warning "CPD encontrou código duplicado (verificar target/cpd.xml)"
fi

# SpotBugs
echo -e "${BLUE}🐛 Executando SpotBugs...${NC}"
mvn spotbugs:check -q
SPOTBUGS_RESULT=$?
if [ $SPOTBUGS_RESULT -eq 0 ]; then
    show_status $SPOTBUGS_RESULT "SpotBugs não encontrou bugs"
else
    show_warning "SpotBugs encontrou possíveis bugs (verificar target/spotbugsXml.xml)"
fi

# JaCoCo Coverage
echo -e "${BLUE}📈 Gerando relatório de cobertura...${NC}"
mvn jacoco:report -q
JACOCO_RESULT=$?
if [ $JACOCO_RESULT -eq 0 ]; then
    # Extrair percentual de cobertura
    if [ -f target/site/jacoco/index.html ]; then
        COVERAGE=$(grep -o "Total[^%]*%" target/site/jacoco/index.html | head -1 | grep -o "[0-9]*%" | head -1)
        show_status $JACOCO_RESULT "Relatório de cobertura gerado (Cobertura: $COVERAGE)"
    else
        show_status $JACOCO_RESULT "Relatório de cobertura gerado"
    fi
else
    show_status $JACOCO_RESULT "Falha ao gerar relatório de cobertura"
fi

# Verificar cobertura mínima
echo -e "${BLUE}🎯 Verificando cobertura mínima...${NC}"
mvn jacoco:check -q
COVERAGE_CHECK=$?
show_status $COVERAGE_CHECK "Cobertura atende aos requisitos mínimos (95%)"

# Gerar site com todos os relatórios
echo -e "${BLUE}🌐 Gerando site com relatórios...${NC}"
mvn site -Pquality -q
SITE_RESULT=$?
show_status $SITE_RESULT "Site com relatórios gerado"

# Resumo final
echo ""
echo "======================================"
echo -e "${BLUE}📊 RESUMO DA ANÁLISE DE QUALIDADE${NC}"
echo "======================================"

if [ $TESTS_RESULT -eq 0 ]; then
    echo -e "🧪 Testes: ${GREEN}PASSOU${NC}"
else
    echo -e "🧪 Testes: ${RED}FALHOU${NC}"
fi

if [ $CHECKSTYLE_RESULT -eq 0 ]; then
    echo -e "✅ CheckStyle: ${GREEN}PASSOU${NC}"
else
    echo -e "✅ CheckStyle: ${YELLOW}AVISOS${NC}"
fi

if [ $PMD_RESULT -eq 0 ]; then
    echo -e "🔍 PMD: ${GREEN}PASSOU${NC}"
else
    echo -e "🔍 PMD: ${YELLOW}AVISOS${NC}"
fi

if [ $CPD_RESULT -eq 0 ]; then
    echo -e "📋 CPD: ${GREEN}PASSOU${NC}"
else
    echo -e "📋 CPD: ${YELLOW}AVISOS${NC}"
fi

if [ $SPOTBUGS_RESULT -eq 0 ]; then
    echo -e "🐛 SpotBugs: ${GREEN}PASSOU${NC}"
else
    echo -e "🐛 SpotBugs: ${YELLOW}AVISOS${NC}"
fi

if [ $COVERAGE_CHECK -eq 0 ]; then
    echo -e "📈 Cobertura: ${GREEN}PASSOU${NC}"
else
    echo -e "📈 Cobertura: ${RED}FALHOU${NC}"
fi

echo ""
echo -e "${BLUE}📋 Relatórios disponíveis em:${NC}"
echo "• Cobertura: target/site/jacoco/index.html"
echo "• CheckStyle: target/site/checkstyle.html"
echo "• PMD: target/site/pmd.html"
echo "• SpotBugs: target/site/spotbugs.html"
echo "• Site completo: target/site/index.html"

echo ""
if [ $TESTS_RESULT -eq 0 ] && [ $COVERAGE_CHECK -eq 0 ]; then
    echo -e "${GREEN}🎉 ANÁLISE DE QUALIDADE CONCLUÍDA COM SUCESSO!${NC}"
    exit 0
else
    echo -e "${RED}⚠️  ANÁLISE CONCLUÍDA COM PROBLEMAS${NC}"
    exit 1
fi