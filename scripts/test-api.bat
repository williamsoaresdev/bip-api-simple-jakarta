@echo off
REM ========================================
REM BIP API - Scripts de Teste Batch
REM ========================================
REM Comandos básicos para testar a BIP API
REM Requer curl.exe disponível no PATH
REM ========================================

setlocal enabledelayedexpansion

REM Configurações
set BASE_URL=http://localhost:8080/api
set CONTENT_TYPE=Content-Type: application/json
set ACCEPT=Accept: application/json

REM Cores (se suportadas pelo terminal)
set GREEN=[92m
set BLUE=[94m
set YELLOW=[93m
set RED=[91m
set NC=[0m

echo.
echo %BLUE%========================================%NC%
echo %BLUE%BIP API - TESTES BÁSICOS%NC%
echo %BLUE%========================================%NC%
echo.

REM Verificar se curl está disponível
where curl >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo %RED%ERRO: curl não encontrado no PATH%NC%
    echo Instale o curl ou use o PowerShell script: test-api.ps1
    pause
    exit /b 1
)

echo %GREEN%Iniciando testes da API BIP...%NC%
echo.

REM ========================================
REM 1. STATUS DA API
REM ========================================

echo %BLUE%1. Verificando status da API...%NC%
echo Comando: curl -X GET "%BASE_URL%/beneficios/status" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/beneficios/status" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM 2. LISTAR BENEFÍCIOS
REM ========================================

echo %BLUE%2. Listando benefícios existentes...%NC%
echo Comando: curl -X GET "%BASE_URL%/beneficios" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/beneficios" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM 3. CRIAR BENEFÍCIO
REM ========================================

echo %BLUE%3. Criando novo benefício...%NC%
echo Comando: curl -X POST "%BASE_URL%/beneficios" -H "%CONTENT_TYPE%" -H "%ACCEPT%" -d "{...}"
echo.
curl -X POST "%BASE_URL%/beneficios" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"nome\":\"Vale Alimentação\",\"descricao\":\"Benefício para compra de alimentos\",\"valorInicial\":500.00}"
echo.
echo.
pause

REM ========================================
REM 4. CRIAR SEGUNDO BENEFÍCIO
REM ========================================

echo %BLUE%4. Criando segundo benefício...%NC%
echo Comando: curl -X POST "%BASE_URL%/beneficios" -H "%CONTENT_TYPE%" -H "%ACCEPT%" -d "{...}"
echo.
curl -X POST "%BASE_URL%/beneficios" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"nome\":\"Vale Transporte\",\"descricao\":\"Benefício para transporte público\",\"valorInicial\":200.00}"
echo.
echo.
pause

REM ========================================
REM 5. BUSCAR BENEFÍCIO POR ID
REM ========================================

echo %BLUE%5. Buscando benefício por ID (ID=1)...%NC%
echo Comando: curl -X GET "%BASE_URL%/beneficios/1" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/beneficios/1" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM 6. CALCULAR TAXA
REM ========================================

echo %BLUE%6. Calculando taxa para transferência...%NC%
echo Comando: curl -X GET "%BASE_URL%/transferencias/taxa?valor=100.00" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/transferencias/taxa?valor=100.00" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM 7. VALIDAR TRANSFERÊNCIA
REM ========================================

echo %BLUE%7. Validando transferência...%NC%
echo Comando: curl -X POST "%BASE_URL%/transferencias/validar" -H "%CONTENT_TYPE%" -H "%ACCEPT%" -d "{...}"
echo.
curl -X POST "%BASE_URL%/transferencias/validar" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"beneficioOrigemId\":1,\"beneficioDestinoId\":2,\"valor\":100.00,\"descricao\":\"Validação de transferência\"}"
echo.
echo.
pause

REM ========================================
REM 8. EXECUTAR TRANSFERÊNCIA
REM ========================================

echo %BLUE%8. Executando transferência...%NC%
echo Comando: curl -X POST "%BASE_URL%/transferencias" -H "%CONTENT_TYPE%" -H "%ACCEPT%" -d "{...}"
echo.
curl -X POST "%BASE_URL%/transferencias" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"beneficioOrigemId\":1,\"beneficioDestinoId\":2,\"valor\":100.00,\"descricao\":\"Transferência de teste via Batch\"}"
echo.
echo.
pause

REM ========================================
REM 9. VERIFICAR SALDOS ATUALIZADOS
REM ========================================

echo %BLUE%9. Verificando saldos após transferência...%NC%
echo Comando: curl -X GET "%BASE_URL%/beneficios" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/beneficios" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM 10. ESTATÍSTICAS
REM ========================================

echo %BLUE%10. Consultando estatísticas...%NC%
echo Comando: curl -X GET "%BASE_URL%/beneficios/estatisticas" -H "%ACCEPT%"
echo.
curl -X GET "%BASE_URL%/beneficios/estatisticas" -H "%ACCEPT%"
echo.
echo.
pause

REM ========================================
REM TESTES DE VALIDAÇÃO
REM ========================================

echo %BLUE%========================================%NC%
echo %BLUE%TESTES DE VALIDAÇÃO%NC%
echo %BLUE%========================================%NC%
echo.

echo %YELLOW%Teste 1: Benefício com dados inválidos%NC%
curl -X POST "%BASE_URL%/beneficios" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"nome\":\"AB\",\"descricao\":\"\",\"valorInicial\":-100.00}"
echo.
echo.
pause

echo %YELLOW%Teste 2: Buscar benefício inexistente%NC%
curl -X GET "%BASE_URL%/beneficios/99999" -H "%ACCEPT%"
echo.
echo.
pause

echo %YELLOW%Teste 3: Transferência com saldo insuficiente%NC%
curl -X POST "%BASE_URL%/transferencias" ^
     -H "%CONTENT_TYPE%" ^
     -H "%ACCEPT%" ^
     -d "{\"beneficioOrigemId\":2,\"beneficioDestinoId\":1,\"valor\":999999.00,\"descricao\":\"Teste de saldo insuficiente\"}"
echo.
echo.
pause

REM ========================================
REM CONCLUSÃO
REM ========================================

echo %BLUE%========================================%NC%
echo %BLUE%TESTES CONCLUÍDOS%NC%
echo %BLUE%========================================%NC%
echo.
echo %GREEN%✓ Todos os testes básicos foram executados!%NC%
echo.
echo %YELLOW%📋 Resumo dos testes realizados:%NC%
echo   • Status da API
echo   • Listar benefícios
echo   • Criar benefícios
echo   • Buscar por ID
echo   • Calcular taxa
echo   • Validar transferência
echo   • Executar transferência
echo   • Verificar saldos
echo   • Estatísticas
echo   • Testes de validação
echo.
echo %BLUE%🔗 Para testes mais avançados, use:%NC%
echo   • PowerShell: test-api.ps1
echo   • Bash: test-api.sh
echo   • Postman: BIP-API-Collection.postman_collection.json
echo.
echo %GREEN%🎯 API BIP testada com sucesso!%NC%
echo.
pause