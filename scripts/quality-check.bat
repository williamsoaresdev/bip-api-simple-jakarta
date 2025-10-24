@echo off
setlocal enabledelayedexpansion

REM ============================================
REM BIP API - Script de Análise de Qualidade
REM ============================================

echo 🔧 Iniciando análise completa de qualidade de código...
echo.

REM Função para mostrar status
:show_status
if %1 equ 0 (
    echo ✅ %~2
) else (
    echo ❌ %~2
    exit /b 1
)
goto :eof

REM Limpar compilações anteriores
echo 🧹 Limpando compilações anteriores...
call mvn clean >nul 2>&1
call :show_status %errorlevel% "Limpeza concluída"

REM Compilar o projeto
echo 🔨 Compilando o projeto...
call mvn compile -q
call :show_status %errorlevel% "Compilação concluída"

REM Executar testes
echo 🧪 Executando testes unitários...
call mvn test -q
set TESTS_RESULT=%errorlevel%
if %TESTS_RESULT% equ 0 (
    call :show_status %TESTS_RESULT% "Testes executados com sucesso"
) else (
    call :show_status %TESTS_RESULT% "Falha nos testes unitários"
)

REM CheckStyle
echo ✅ Executando CheckStyle...
call mvn checkstyle:check -q
set CHECKSTYLE_RESULT=%errorlevel%
if %CHECKSTYLE_RESULT% equ 0 (
    call :show_status %CHECKSTYLE_RESULT% "CheckStyle passou sem violações"
) else (
    echo ⚠️  CheckStyle encontrou violações (verificar target\checkstyle-result.xml)
)

REM PMD
echo 🔍 Executando PMD...
call mvn pmd:check -q
set PMD_RESULT=%errorlevel%
if %PMD_RESULT% equ 0 (
    call :show_status %PMD_RESULT% "PMD passou sem problemas"
) else (
    echo ⚠️  PMD encontrou issues (verificar target\pmd.xml)
)

REM CPD (Copy-Paste Detector)
echo 📋 Executando CPD (detecção de duplicação)...
call mvn pmd:cpd-check -q
set CPD_RESULT=%errorlevel%
if %CPD_RESULT% equ 0 (
    call :show_status %CPD_RESULT% "CPD não encontrou duplicações"
) else (
    echo ⚠️  CPD encontrou código duplicado (verificar target\cpd.xml)
)

REM SpotBugs
echo 🐛 Executando SpotBugs...
call mvn spotbugs:check -q
set SPOTBUGS_RESULT=%errorlevel%
if %SPOTBUGS_RESULT% equ 0 (
    call :show_status %SPOTBUGS_RESULT% "SpotBugs não encontrou bugs"
) else (
    echo ⚠️  SpotBugs encontrou possíveis bugs (verificar target\spotbugsXml.xml)
)

REM JaCoCo Coverage
echo 📈 Gerando relatório de cobertura...
call mvn jacoco:report -q
set JACOCO_RESULT=%errorlevel%
call :show_status %JACOCO_RESULT% "Relatório de cobertura gerado"

REM Verificar cobertura mínima
echo 🎯 Verificando cobertura mínima...
call mvn jacoco:check -q
set COVERAGE_CHECK=%errorlevel%
call :show_status %COVERAGE_CHECK% "Cobertura atende aos requisitos mínimos (95%%)"

REM Gerar site com todos os relatórios
echo 🌐 Gerando site com relatórios...
call mvn site -Pquality -q
set SITE_RESULT=%errorlevel%
call :show_status %SITE_RESULT% "Site com relatórios gerado"

REM Resumo final
echo.
echo ======================================
echo 📊 RESUMO DA ANÁLISE DE QUALIDADE
echo ======================================

if %TESTS_RESULT% equ 0 (
    echo 🧪 Testes: PASSOU
) else (
    echo 🧪 Testes: FALHOU
)

if %CHECKSTYLE_RESULT% equ 0 (
    echo ✅ CheckStyle: PASSOU
) else (
    echo ✅ CheckStyle: AVISOS
)

if %PMD_RESULT% equ 0 (
    echo 🔍 PMD: PASSOU
) else (
    echo 🔍 PMD: AVISOS
)

if %CPD_RESULT% equ 0 (
    echo 📋 CPD: PASSOU
) else (
    echo 📋 CPD: AVISOS
)

if %SPOTBUGS_RESULT% equ 0 (
    echo 🐛 SpotBugs: PASSOU
) else (
    echo 🐛 SpotBugs: AVISOS
)

if %COVERAGE_CHECK% equ 0 (
    echo 📈 Cobertura: PASSOU
) else (
    echo 📈 Cobertura: FALHOU
)

echo.
echo 📋 Relatórios disponíveis em:
echo • Cobertura: target\site\jacoco\index.html
echo • CheckStyle: target\site\checkstyle.html
echo • PMD: target\site\pmd.html
echo • SpotBugs: target\site\spotbugs.html
echo • Site completo: target\site\index.html
echo • Configurações: config\quality\

echo.
if %TESTS_RESULT% equ 0 if %COVERAGE_CHECK% equ 0 (
    echo 🎉 ANÁLISE DE QUALIDADE CONCLUÍDA COM SUCESSO!
    exit /b 0
) else (
    echo ⚠️  ANÁLISE CONCLUÍDA COM PROBLEMAS
    exit /b 1
)

endlocal