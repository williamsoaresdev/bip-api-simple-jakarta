@echo off
rem =================================================
rem Script de Configuração do WildFly para BIP API (Windows)
rem =================================================

echo 🚀 === CONFIGURACAO WILDFLY PARA BIP API ===
echo Data: %date% %time%
echo.

set WILDFLY_HOME=%WILDFLY_HOME%
if "%WILDFLY_HOME%"=="" set WILDFLY_HOME=C:\wildfly

if not exist "%WILDFLY_HOME%\bin\jboss-cli.bat" (
    echo ❌ WildFly nao encontrado em: %WILDFLY_HOME%
    echo    Configure a variavel WILDFLY_HOME ou instale o WildFly
    pause
    exit /b 1
)

echo ✅ WildFly encontrado: %WILDFLY_HOME%
echo.

echo 1. Verificando status do WildFly...
tasklist /FI "IMAGENAME eq java.exe" | findstr "java.exe" >nul
if errorlevel 1 (
    echo    Iniciando WildFly...
    start "WildFly" "%WILDFLY_HOME%\bin\standalone.bat" --server-config=standalone-full.xml
    echo    Aguardando inicializacao...
    timeout /t 20 /nobreak >nul
) else (
    echo    ✅ Java ja esta rodando
)

echo.
echo 2. Configurando DataSource H2...

rem Configurar DataSource H2
"%WILDFLY_HOME%\bin\jboss-cli.bat" --connect --commands="/subsystem=datasources/data-source=BipDS:add(jndi-name=java:jboss/datasources/BipDS,driver-name=h2,connection-url=jdbc:h2:mem:bipdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE,user-name=sa,password=,min-pool-size=5,max-pool-size=20,pool-prefill=true,transaction-isolation=TRANSACTION_READ_COMMITTED,enabled=true)"

if %errorlevel%==0 (
    echo    ✅ DataSource BipDS criado com sucesso
) else (
    echo    ⚠️ DataSource pode ja existir ^(ignorando erro^)
)

echo.
echo 3. Configurando Logging...

rem Configurar logging
"%WILDFLY_HOME%\bin\jboss-cli.bat" --connect --commands="/subsystem=logging/logger=com.bip:add(level=INFO),/subsystem=logging/logger=com.bip.ejb:add(level=DEBUG)"

echo    ✅ Logging configurado

echo.
echo 4. Testando DataSource...

rem Testar conexão
"%WILDFLY_HOME%\bin\jboss-cli.bat" --connect --command="/subsystem=datasources/data-source=BipDS:test-connection-in-pool"

if %errorlevel%==0 (
    echo    ✅ DataSource testado com sucesso
) else (
    echo    ❌ Erro ao testar DataSource
)

echo.
echo 5. Configuracoes aplicadas:
echo    - DataSource: java:jboss/datasources/BipDS
echo    - Banco: H2 em memoria
echo    - Pool: 5-20 conexoes
echo    - Logging: INFO para com.bip
echo.

echo 🎯 === PROXIMOS PASSOS ===
echo 1. Compilar aplicacao:
echo    cd jakarta-ee ^&^& mvn clean package
echo.
echo 2. Deploy da aplicacao:
echo    mvn wildfly:deploy
echo.
echo 3. Ou deploy manual:
echo    copy target\bip-api-jakarta-ee.war "%WILDFLY_HOME%\standalone\deployments\"
echo.
echo 4. Acessar aplicacao:
echo    http://localhost:8080/bip-api-jakarta-ee/
echo.
echo 5. Management Console:
echo    http://localhost:9990
echo.

echo ✅ Configuracao concluida!
pause